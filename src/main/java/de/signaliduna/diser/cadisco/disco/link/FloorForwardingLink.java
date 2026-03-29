package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import reactor.core.publisher.Mono;

import java.util.List;

public class FloorForwardingLink implements ChainLink<GuestData, GuestData, DiscoContext> {

    @Override
    public Mono<GuestData> transform(Mono<GuestData> input, DiscoContext context) {
        return input.map(guestData -> {
            List<String> floorNames = context.getSelectedFloors().stream()
                    .map(floor -> floor.floorName())
                    .toList();
            context.setVisitedFloors(floorNames);
            floorNames.forEach(name ->
                    context.log("Gast " + guestData.guestId() + " betritt Floor: " + name));
            return guestData;
        });
    }
}
