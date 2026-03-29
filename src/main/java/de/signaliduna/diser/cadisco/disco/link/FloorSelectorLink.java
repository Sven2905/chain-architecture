package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import de.signaliduna.diser.cadisco.disco.service.DjService;
import reactor.core.publisher.Mono;

public class FloorSelectorLink implements ChainLink<GuestData, GuestData, DiscoContext> {

    private final DjService djService;

    public FloorSelectorLink(DjService djService) {
        this.djService = djService;
    }

    @Override
    public Mono<GuestData> transform(Mono<GuestData> input, DiscoContext context) {
        return input.flatMap(guestData ->
                djService.getAvailableFloors().map(floors -> {
                    context.setAvailableFloors(floors);
                    context.log(floors.size() + " Floors vom DJ geladen.");
                    return guestData;
                })
        );
    }
}
