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
                            .map(data -> ProcessedFloor.builder()
                                    .definition(floor)
                                    .extractedData(data)
                                    .successful(true)
                                    .build())
                            .onErrorResume(e -> Mono.just(ProcessedFloor.builder()
                                    .definition(floor)
                                    .successful(false)
                                    .errorMessage(e.getMessage())
                                    .build())))
                    .doOnNext(ctx::addResult)
                    .collectList();
        });
    }
}
