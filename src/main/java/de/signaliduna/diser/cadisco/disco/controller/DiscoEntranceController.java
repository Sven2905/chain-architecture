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

/**
 * Der Eingang zur Disco -- hier beginnt und endet der HTTP-Request.
 *
 * <p>Orchestriert den gesamten Disco-Besuch in zwei Phasen:</p>
 * <ul>
 *   <li><strong>Phase 1 (synchron):</strong> Security-Check via {@link SecurityCheckLink}.
 *       Bestimmt die HTTP-Response: {@code 200 ADMITTED} oder {@code 403 DENIED}.</li>
 *   <li><strong>Phase 2 (asynchron):</strong> Die eigentliche Disco-Nacht. Wird per
 *       {@code subscribeOn(Schedulers.boundedElastic())} im Hintergrund gestartet,
 *       sobald Phase 1 erfolgreich war. Die HTTP-Response ist laengst zurueck,
 *       waehrend die Nacht gerade erst beginnt -- genau wie im echten Leben.</li>
 * </ul>
 *
 * <p>Fehler in Phase 2 werden geloggt, erreichen aber nie die HTTP-Response.
 * Was in der Disco passiert, bleibt in der Disco.</p>
 *
 * @see Chain
 * @see DiscoContext
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DiscoEntranceController {

    private final DjService djService;
    private final ArchivPlusService archivPlusService;
    private final IcdosService icdosService;

    /**
     * Eingangs-Endpunkt fuer Disco-Gaeste.
     *
     * <p>Nimmt einen {@link DiscoEntryRequest} entgegen, fuehrt den Security-Check durch,
     * und startet bei Erfolg die asynchrone Disco-Nacht im Hintergrund.</p>
     *
     * @param request der Eingangs-Request mit Gast-ID und Region
     * @return {@code 200} mit ADMITTED-Response oder {@code 403} mit DENIED-Response
     */
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

    /**
     * Startet die asynchrone Disco-Nacht (Phase 2) im Hintergrund.
     *
     * <p>Baut eine vollstaendige Chain aus fuenf Links und subscribt auf
     * {@code Schedulers.boundedElastic()}, damit die Netty-Event-Loop-Threads
     * nicht blockiert werden. Fire-and-forget in seiner elegantesten Form.</p>
     *
     * @param input der Request als reaktiver Stream
     * @param ctx   der Disco-Context (bereits durch Phase 1 initialisiert)
     */
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
