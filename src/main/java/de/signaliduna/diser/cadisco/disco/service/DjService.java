package de.signaliduna.diser.cadisco.disco.service;

import de.signaliduna.diser.cadisco.disco.config.DiscoConfigProperties;
import de.signaliduna.diser.cadisco.disco.model.FloorDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Der DJ der Disco -- entscheidet, auf welchem Floor die Musik spielt.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DjService {

    private final DiscoConfigProperties config;

    public List<FloorDefinition> getDefaultFloors() {
        return config.getAsList();
    }

    public Mono<List<FloorDefinition>> identifyFloors(List<String> docs, List<String> contracts, List<FloorDefinition> availableFloors) {
        log.info("DJ: Identification started for {} docs and {} contracts", docs.size(), contracts.size());
        if (!availableFloors.isEmpty()) {
            return Mono.just(List.of(availableFloors.getFirst()));
        }
        return Mono.just(List.of());
    }

    public Mono<String> extractFloorRequirements(FloorDefinition definition, List<String> docs, List<String> contracts) {
        log.info("DJ: Extracting requirements for floor '{}'", definition.getName());
        return Mono.just("{\"floor\": \"" + definition.getName() + "\", \"groove\": \"EXTRACTED\"}");
    }
}
