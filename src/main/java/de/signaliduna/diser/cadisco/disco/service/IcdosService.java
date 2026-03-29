package de.signaliduna.diser.cadisco.disco.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Anbindung an das Icdos Legacy-System.
 *
 * <p>Verifiziert Gastdatensaetze in einem weiteren Altsystem. Gibt zurueck,
 * ob der Gast im System bekannt und verifiziert ist. Aktuell antwortet
 * der Service stets mit {@code true} -- eine erfrischend positive Lebenseinstellung
 * fuer ein Legacy-System.</p>
 *
 * <p>150ms simulierte Latenz. Parallel mit ArchivPlus via {@code Mono.zip()}
 * aufgerufen, damit man nicht doppelt wartet. Effizienz ist schliesslich kein Zufall.</p>
 */
@Service
public class IcdosService {

    public Mono<Boolean> verifyGuestRecord(String guestId) {
        return Mono.just(true)
                .delayElement(Duration.ofMillis(150));
    }
}
