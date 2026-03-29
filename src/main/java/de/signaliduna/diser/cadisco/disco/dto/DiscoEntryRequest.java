package de.signaliduna.diser.cadisco.disco.dto;

/**
 * Eingangs-Request eines Disco-Gastes.
 *
 * <p>Immutable, wie es sich fuer Daten gehoert, die durch eine Chain fliessen.
 * Enthaelt alles, was der Bouncer wissen muss: Wer bist du, und woher kommst du.</p>
 *
 * @param guestId die eindeutige Gast-ID
 * @param region  die Herkunftsregion des Gastes (bestimmt, ob der Einlass gewaehrt wird)
 */
public record DiscoEntryRequest(String guestId, String region) {
}
