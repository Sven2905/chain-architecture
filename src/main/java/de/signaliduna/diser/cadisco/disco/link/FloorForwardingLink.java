package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import de.signaliduna.diser.cadisco.disco.model.ProcessedFloor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Leitet den Gast auf den richtigen Dancefloor weiter.
 */
public class FloorForwardingLink implements ChainLink<Mono<List<ProcessedFloor>>, Mono<List<ProcessedFloor>>, DiscoContext> {

    @Override
    public Mono<List<ProcessedFloor>> process(Mono<List<ProcessedFloor>> resultsMono, DiscoContext ctx) {
        return resultsMono.flatMap(results -> {
            ctx.log("Security: Directing guest to " + results.size() + " dancefloors...");
            return Flux.fromIterable(results)
                    .filter(ProcessedFloor::isSuccessful)
                    .doOnNext(res -> ctx.log("Dancefloor: Opening door to '" + res.getDefinition().getName() + "' at: " + res.getDefinition().getUrl()))
                    .collectList()
                    .thenReturn(results);
        });
    }
}
