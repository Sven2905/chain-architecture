package de.signaliduna.diser.cadisco.disco.service;

import de.signaliduna.diser.cadisco.disco.dto.FloorInfo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class DjService {

    private static final List<FloorInfo> ALL_FLOORS = List.of(
            new FloorInfo("Main Hall", 128, 18, false),
            new FloorInfo("Techno Keller", 140, 21, false),
            new FloorInfo("VIP Lounge", 110, 18, true),
            new FloorInfo("Chill Zone", 90, 16, false),
            new FloorInfo("Rave Arena", 150, 21, true)
    );

    public Mono<List<FloorInfo>> getAvailableFloors() {
        return Mono.just(ALL_FLOORS)
                .delayElement(Duration.ofMillis(100));
    }
}
