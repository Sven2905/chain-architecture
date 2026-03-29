package de.signaliduna.diser.cadisco.disco.dto;

/**
 * Vollstaendige Gastdaten, angereichert aus den Legacy-Systemen.
 *
 * <p>Was der Bouncer nicht weiss, wissen ArchivPlus und Icdos.
 * Dieses Record traegt alle relevanten Informationen fuer die Floor-Auswahl.</p>
 *
 * @param guestId die eindeutige Gast-ID
 * @param name    der Name des Gastes
 * @param region  die Herkunftsregion
 * @param age     das Alter -- entscheidend fuer den Zugang zu gewissen Floors
 * @param vip     VIP-Status -- oeffnet Tueren, die anderen verschlossen bleiben
 */
public record GuestData(String guestId, String name, String region, int age, boolean vip) {
}
