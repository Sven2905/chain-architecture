package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.core.ChainLinkException;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.DiscoEntryRequest;
import reactor.core.publisher.Mono;

import java.util.Set;

public class SecurityCheckLink implements ChainLink<DiscoEntryRequest, DiscoEntryRequest, DiscoContext> {

    private static final Set<String> BLOCKED_REGIONS = Set.of("BLOCKED", "BANNED");

    @Override
    public Mono<DiscoEntryRequest> transform(Mono<DiscoEntryRequest> input, DiscoContext context) {
        return input.flatMap(request -> {
            if (request.guestId() == null || request.guestId().isBlank()) {
                return Mono.error(new ChainLinkException(
                        "Gast-ID fehlt.", "SecurityCheckLink", context.getCorrelationId()));
            }
            if (request.region() == null || BLOCKED_REGIONS.contains(request.region().toUpperCase())) {
                context.setStatus("DENIED");
                return Mono.error(new ChainLinkException(
                        "Region '" + request.region() + "' ist gesperrt.", "SecurityCheckLink", context.getCorrelationId()));
            }
            context.setStatus("ADMITTED");
            context.log("Gast " + request.guestId() + " aus Region " + request.region() + " zugelassen.");
            return Mono.just(request);
        });
    }
}
