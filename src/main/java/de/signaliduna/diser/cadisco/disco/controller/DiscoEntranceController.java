package de.signaliduna.diser.cadisco.disco.controller;

import de.signaliduna.diser.cadisco.core.Chain;
import de.signaliduna.diser.cadisco.core.ChainLinkException;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.DiscoEntryRequest;
import de.signaliduna.diser.cadisco.disco.dto.DiscoEntryResponse;
import de.signaliduna.diser.cadisco.disco.link.*;
import de.signaliduna.diser.cadisco.disco.service.ArchivPlusService;
import de.signaliduna.diser.cadisco.disco.service.DjService;
import de.signaliduna.diser.cadisco.disco.service.IcdosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiscoEntranceController {

    private final DjService djService;
    private final ArchivPlusService archivPlusService;
    private final IcdosService icdosService;

    @PostMapping("/disco/entry")
    public Mono<ResponseEntity<DiscoEntryResponse>> enter(@RequestBody DiscoEntryRequest request) {
        DiscoContext ctx = new DiscoContext(request.guestId(), request.region());

        // Phase 1: Synchroner Security-Check
        return Chain.start(Mono.just(request), ctx)
                .link(new SecurityCheckLink())
                .execute()
                .map(checkedRequest -> {
                    // Phase 2: Asynchrone Disco-Nacht im Hintergrund
                    startDiscoNight(Mono.just(checkedRequest), ctx);
                    return ResponseEntity.ok(new DiscoEntryResponse(
                            request.guestId(),
                            "ADMITTED",
                            "Willkommen in der Disco, " + request.guestId() + "!"
                    ));
                })
                .onErrorResume(ChainLinkException.class, ex -> {
                    log.warn("[{}] Einlass verweigert: {}", ctx.getCorrelationId(), ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new DiscoEntryResponse(
                                    request.guestId(),
                                    "DENIED",
                                    ex.getMessage()
                            )));
                });
    }

    private void startDiscoNight(Mono<DiscoEntryRequest> input, DiscoContext ctx) {
        Chain.start(input, ctx)
                .link(new GuestDataSyncLink(archivPlusService, icdosService))
                .link(new FloorSelectorLink(djService))
                .link(new FloorRequirementLink())
                .link(new FloorForwardingLink())
                .link(new DiscoReportLink())
                .execute()
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(report -> ctx.log("Disco-Nacht komplett abgeschlossen."))
                .doOnError(error -> log.error("[{}] Fehler in Phase 2: {}", ctx.getCorrelationId(), error.getMessage()))
                .subscribe();
    }
}
