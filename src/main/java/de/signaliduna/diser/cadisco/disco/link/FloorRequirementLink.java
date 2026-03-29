package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import de.signaliduna.diser.cadisco.disco.model.FloorDefinition;
import de.signaliduna.diser.cadisco.disco.model.ProcessedFloor;
import de.signaliduna.diser.cadisco.disco.service.DjService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Ermittelt die Anforderungen ("Dresscode") fuer jeden Floor.
 */
@RequiredArgsConstructor
public class FloorRequirementLink implements ChainLink<Mono<List<FloorDefinition>>, Mono<List<ProcessedFloor>>, DiscoContext> {

    private final DjService djService;

    @Override
    public Mono<List<ProcessedFloor>> process(Mono<List<FloorDefinition>> floorsMono, DiscoContext ctx) {
        return floorsMono.flatMap(floors -> {
            ctx.log("DJ: Syncing dresscode for " + floors.size() + " floors...");
            return Flux.fromIterable(floors)
                    .flatMap(floor -> djService.extractFloorRequirements(floor, ctx.getDownloadedDocuments(), ctx.getRetrievedContracts())
                            .map(data -> ProcessedFloor.success(floor, data))
                            .onErrorResume(e -> Mono.just(ProcessedFloor.failure(floor, e.getMessage()))))
                    .doOnNext(ctx::addResult)
                    .collectList();
        });
    }
}
