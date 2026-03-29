package de.signaliduna.diser.cadisco.disco.controller;

import de.signaliduna.diser.cadisco.core.Chain;
import de.signaliduna.diser.cadisco.disco.link.*;
import de.signaliduna.diser.cadisco.disco.model.AMSInput;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import de.signaliduna.diser.cadisco.disco.model.DiscoResponse;
import de.signaliduna.diser.cadisco.disco.service.ArchivPlusService;
import de.signaliduna.diser.cadisco.disco.service.DjService;
import de.signaliduna.diser.cadisco.disco.service.IcdosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Der Eingangsbereich der Disco.
 * Orchestrierung via Fluent-API: Phase 1 synchron, Phase 2 fire-and-forget.
 */
@Slf4j
@RestController
@RequestMapping("/disco")
@RequiredArgsConstructor
public class DiscoEntranceController {

    private final ArchivPlusService archivPlusService;
    private final IcdosService icdosService;
    private final DjService djService;

    @PostMapping("/entry")
    public Mono<DiscoResponse> enterDisco(@RequestBody AMSInput request) {
        log.info("Disco: Entry request received for guest {} from region {}", request.getGlobalId(), request.getSparte());

        DiscoContext ctx = new DiscoContext(request.getGlobalId());
        ctx.setAvailableFloors(djService.getDefaultFloors());
        ctx.log("Disco: Guest " + request.getName() + " arrived at the entrance.");

        // Phase 1: Security Check (Bouncer)
        return Chain.start(Mono.just(request), ctx)
                .link(new SecurityCheckLink())
                .execute()
                .map(guest -> {
                    // Phase 2: Die eigentliche "Nacht" im Hintergrund
                    startDiscoNight(Mono.just(guest), ctx);
                    return new DiscoResponse(request.getGlobalId(), DiscoResponse.State.IN_BEARBEITUNG);
                })
                .onErrorResume(e -> {
                    log.error("Bouncer: Permission denied for {}: {}", request.getGlobalId(), e.getMessage());
                    return Mono.just(new DiscoResponse(request.getGlobalId(), DiscoResponse.State.NICHT_ZUSTÄNDIG));
                });
    }

    private void startDiscoNight(Mono<AMSInput> guestMono, DiscoContext ctx) {
        Chain.start(guestMono, ctx)
                .link(new GuestDataSyncLink(archivPlusService, icdosService))
                .link(new FloorSelectorLink(djService))
                .link(new FloorRequirementLink(djService))
                .link(new FloorForwardingLink())
                .link(new DiscoReportLink())
                .execute()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        res -> log.info("Disco: Night ended successfully for {}!", res.getGlobalId()),
                        err -> log.error("Disco: Night ended with drama for {}: {}", ctx.getGlobalId(), err.getMessage())
                );
    }
}
