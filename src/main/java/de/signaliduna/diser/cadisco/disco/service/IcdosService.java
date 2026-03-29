package de.signaliduna.diser.cadisco.disco.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class IcdosService {

    public Mono<Boolean> verifyGuestRecord(String guestId) {
        return Mono.just(true)
                .delayElement(Duration.ofMillis(150));
    }
}
