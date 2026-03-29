package de.signaliduna.diser.cadisco.disco.dto;

/**
 * Antwort auf einen Disco-Einlass-Versuch.
 *
 * <p>Teilt dem Gast hoeflich -- oder weniger hoeflich -- mit, ob er willkommen ist.</p>
 *
 * @param guestId die Gast-ID aus dem Request
 * @param status  {@code "ADMITTED"} oder {@code "DENIED"} -- tertium non datur
 * @param message eine menschenlesbare Nachricht (weil Maschinen ohnehin den Status lesen)
 */
public record DiscoEntryResponse(String guestId, String status, String message) {
}
