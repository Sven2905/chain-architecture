package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.DiscoEntryRequest;
import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import de.signaliduna.diser.cadisco.disco.service.ArchivPlusService;
import de.signaliduna.diser.cadisco.disco.service.IcdosService;
import reactor.core.publisher.Mono;

/**
 * Synchronisiert Gastdaten mit den Legacy-Systemen -- erstes Glied der Nacht-Kette (Phase 2).
 *
 * <p>Ruft parallel {@link ArchivPlusService} und {@link IcdosService} via
 * {@code Mono.zip()} auf. Beide Aufrufe laufen gleichzeitig, weil sequentielles
 * Warten auf Legacy-Systeme eine Form der Selbstbestrafung ist, die niemand verdient hat.</p>
 *
 * <p>Das Ergebnis wird im {@link DiscoContext} hinterlegt und als angereichertes
 * {@link GuestData} an das naechste Glied weitergereicht.</p>
 */
public class GuestDataSyncLink implements ChainLink<DiscoEntryRequest, GuestData, DiscoContext> {

    private final ArchivPlusService archivPlusService;
    private final IcdosService icdosService;

    public GuestDataSyncLink(ArchivPlusService archivPlusService, IcdosService icdosService) {
        this.archivPlusService = archivPlusService;
        this.icdosService = icdosService;
    }

    @Override
    public Mono<GuestData> transform(Mono<DiscoEntryRequest> input, DiscoContext context) {
        return input.flatMap(request ->
                Mono.zip(
                        archivPlusService.fetchGuestData(request.guestId(), request.region()),
                        icdosService.verifyGuestRecord(request.guestId())
                ).map(tuple -> {
                    GuestData guestData = tuple.getT1();
                    boolean verified = tuple.getT2();
                    context.setGuestData(guestData);
                    context.log("Daten synchronisiert: " + guestData.name() + ", verifiziert=" + verified);
                    return guestData;
                })
        );
    }
}
