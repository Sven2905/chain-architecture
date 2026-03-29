package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import de.signaliduna.diser.cadisco.disco.model.DiscoResponse;
import de.signaliduna.diser.cadisco.disco.model.ProcessedFloor;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Erstellt den Abschlussbericht der Disco-Nacht.
 */
public class DiscoReportLink implements ChainLink<Mono<List<ProcessedFloor>>, Mono<DiscoResponse>, DiscoContext> {

    @Override
    public Mono<DiscoResponse> process(Mono<List<ProcessedFloor>> resultsMono, DiscoContext ctx) {
        return resultsMono.map(results -> {
            long successCount = results.stream().filter(ProcessedFloor::isSuccessful).count();
            long totalCount = results.size();

            DiscoResponse.State finalState = (totalCount > 0 && successCount == totalCount)
                    ? DiscoResponse.State.VERARBEITET
                    : DiscoResponse.State.TEILWEISE_VERARBEITET;

            ctx.log("Disco-Report: Guest's night was " + finalState + " (" + successCount + "/" + totalCount + " floors rocked)");
            return new DiscoResponse(ctx.getGlobalId(), finalState);
        });
    }
}
