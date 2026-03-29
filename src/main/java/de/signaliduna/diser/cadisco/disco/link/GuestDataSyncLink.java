package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.model.AMSInput;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import de.signaliduna.diser.cadisco.disco.service.ArchivPlusService;
import de.signaliduna.diser.cadisco.disco.service.IcdosService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Synchronisiert Gastdaten aus Archiv+ und ICDOS.
 * Parallele Aufrufe mit Retry und Fallback -- weil Legacy-Systeme Geduld erfordern.
 */
@RequiredArgsConstructor
public class GuestDataSyncLink implements ChainLink<Mono<AMSInput>, Mono<AMSInput>, DiscoContext> {

    private final ArchivPlusService archivPlusService;
    private final IcdosService icdosService;

    @Override
    public Mono<AMSInput> process(Mono<AMSInput> guestMono, DiscoContext ctx) {
        return guestMono.flatMap(guest -> {
            ctx.log("Sync: Retrieving guest details from Archiv+ and Icdos...");

            Mono<Void> archivSync = archivPlusService.requestDocuments(guest.getGlobalId())
                    .then(pollArchivPlus(guest.getGlobalId(), ctx))
                    .flatMap(archivPlusService::getDownloadLink)
                    .doOnNext(ctx::addDocument)
                    .then();

            Mono<Void> icdosSync = icdosService.getContracts(guest.getContractNumbers())
                    .flatMapIterable(list -> list)
                    .doOnNext(ctx::addContract)
                    .then();

            return Mono.zip(archivSync, icdosSync)
                    .thenReturn(guest)
                    .onErrorMap(e -> new RuntimeException("Sync: Data retrieval failed. " + e.getMessage()));
        });
    }

    private Mono<String> pollArchivPlus(String guestId, DiscoContext ctx) {
        return Mono.defer(() -> {
            ctx.log("Sync: Waiting for Archiv+ release...");
            return archivPlusService.getArchiveStatus(guestId)
                    .flatMap(status -> "COMPLETED".equals(status)
                            ? Mono.just(status)
                            : Mono.error(new RuntimeException("Pending")));
        })
        .retryWhen(reactor.util.retry.Retry.fixedDelay(3, Duration.ofSeconds(2)))
        .onErrorResume(e -> {
            ctx.log("Sync: Archiv+ timeout. Using Emergency-Fallback.");
            return archivPlusService.fallbackDocuments();
        });
    }
}
