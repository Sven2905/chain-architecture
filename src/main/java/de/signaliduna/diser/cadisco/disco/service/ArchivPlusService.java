package de.signaliduna.diser.cadisco.disco.service;

import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Anbindung an das ArchivPlus Legacy-System.
 *
 * <p>Simuliert den Abruf von Gastdaten aus einem Altsystem, das vermutlich
 * aelter ist als die meisten Entwickler, die es warten muessen.
 * Liefert angereicherte {@link de.signaliduna.diser.cadisco.disco.dto.GuestData}
 * mit 200ms simulierter Latenz -- ein optimistischer Wert fuer ein Legacy-System,
 * wenn man ehrlich ist.</p>
 */
@Service
public class ArchivPlusService {

    public Mono<GuestData> fetchGuestData(String guestId, String region) {
        return Mono.just(new GuestData(guestId, "Gast-" + guestId, region, 25, false))
                .delayElement(Duration.ofMillis(200));
    }
}
