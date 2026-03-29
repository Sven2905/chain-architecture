package de.signaliduna.diser.cadisco.disco.dto;

/**
 * Beschreibung einer Tanzflaeche.
 *
 * <p>Jeder Floor hat seine eigene Persoenlichkeit: BPM-Vorgabe vom DJ,
 * Mindestalter, und die Frage, ob man VIP sein muss, um reinzukommen.
 * Demokratie hat auf der Tanzflaeche eben ihre Grenzen.</p>
 *
 * @param floorName der Name des Floors
 * @param bpm       Beats per Minute -- das Tempo der Musik
 * @param minAge    Mindestalter fuer den Zugang
 * @param vipOnly   {@code true}, wenn nur VIP-Gaeste Zugang haben
 */
public record FloorInfo(String floorName, int bpm, int minAge, boolean vipOnly) {
}
