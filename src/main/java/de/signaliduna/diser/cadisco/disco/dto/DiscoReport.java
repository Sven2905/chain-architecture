package de.signaliduna.diser.cadisco.disco.dto;

import java.time.Instant;
import java.util.List;

/**
 * Abschlussbericht einer Disco-Nacht.
 *
 * <p>Enthaelt alles, was man am naechsten Morgen wissen moechte:
 * welche Floors besucht wurden, was passiert ist, und wann der Spass
 * offiziell vorbei war. Quasi das Protokoll einer gut verbrachten Nacht.</p>
 *
 * @param guestId       die Gast-ID
 * @param visitedFloors die Liste der besuchten Tanzflaechen
 * @param logs          das vollstaendige Verlaufsprotokoll der Nacht
 * @param timestamp     Zeitpunkt der Berichterstellung
 */
public record DiscoReport(String guestId, List<String> visitedFloors, List<String> logs, Instant timestamp) {
}
