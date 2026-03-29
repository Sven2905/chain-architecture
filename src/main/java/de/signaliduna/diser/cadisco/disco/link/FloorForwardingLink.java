package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Leitet den Gast auf die ausgewaehlten Tanzflaechen weiter -- viertes Glied der Nacht-Kette.
 *
 * <p>Nimmt die im {@link DiscoContext} hinterlegten {@code selectedFloors}
 * und simuliert das Betreten jedes Floors. Jeder Eintritt wird einzeln
 * im Context-Log protokolliert -- schliesslich will man spaeter wissen,
 * wo man ueberall war.</p>
 */
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
