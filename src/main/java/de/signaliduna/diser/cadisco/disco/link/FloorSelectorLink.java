package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.model.AMSInput;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import de.signaliduna.diser.cadisco.disco.model.FloorDefinition;
import de.signaliduna.diser.cadisco.disco.service.DjService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Der DJ entscheidet, auf welchen Floor der Gast passt.
 */
@RequiredArgsConstructor
public class FloorSelectorLink implements ChainLink<Mono<AMSInput>, Mono<List<FloorDefinition>>, DiscoContext> {

    private final DjService djService;

    @Override
    public Mono<List<FloorDefinition>> process(Mono<AMSInput> guestMono, DiscoContext ctx) {
        return guestMono.flatMap(guest -> {
            ctx.log("DJ: Analyzing guest's groove for floor selection...");
            return djService.identifyFloors(
                    ctx.getDownloadedDocuments(),
                    ctx.getRetrievedContracts(),
                    ctx.getAvailableFloors()
            ).doOnNext(floors -> ctx.log("DJ: Recommended " + floors.size() + " floors for tonight."));
        });
    }
}
