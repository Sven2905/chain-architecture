package de.signaliduna.diser.cadisco.disco.service;

import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ArchivPlusService {

    public Mono<GuestData> fetchGuestData(String guestId, String region) {
        return Mono.just(new GuestData(guestId, "Gast-" + guestId, region, 25, false))
                .delayElement(Duration.ofMillis(200));
    }
}
