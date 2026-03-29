package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.FloorInfo;
import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import reactor.core.publisher.Mono;

import java.util.List;

public class FloorRequirementLink implements ChainLink<GuestData, GuestData, DiscoContext> {

    @Override
    public Mono<GuestData> transform(Mono<GuestData> input, DiscoContext context) {
        return input.map(guestData -> {
            List<FloorInfo> eligible = context.getAvailableFloors().stream()
                    .filter(floor -> guestData.age() >= floor.minAge())
                    .filter(floor -> !floor.vipOnly() || guestData.vip())
                    .toList();
            context.setSelectedFloors(eligible);
            context.log(eligible.size() + " Floors nach Anforderungsprüfung verfügbar (Alter=" + guestData.age() + ", VIP=" + guestData.vip() + ").");
            return guestData;
        });
    }
}
