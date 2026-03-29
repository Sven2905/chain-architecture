package de.signaliduna.diser.cadisco.core;

import lombok.Getter;

/**
 * Das "Broken Link" -- wenn ein Glied der Kette reisst.
 *
 * <p>Wird von einem {@link ChainLink} geworfen, um die Kette sofort zu stoppen.
 * Der {@link Chain Orchestrator} faengt den Fehler zentral ab. Traegt den Namen
 * des fehlgeschlagenen Links und die Correlation-ID, damit man im Log nicht
 * raten muss, was schiefging.</p>
 *
 * <p>Fail-Fast-Prinzip: Lieber frueh und laut scheitern als leise und spaet.
 * Eine Lebensweisheit, die auch ausserhalb der Softwareentwicklung gilt.</p>
 */
@Getter
public class ChainLinkException extends RuntimeException {

    private final String linkName;
    private final String correlationId;

    public ChainLinkException(String message, String linkName, String correlationId) {
        super(message);
        this.linkName = linkName;
        this.correlationId = correlationId;
    }

    public ChainLinkException(String message, String linkName, String correlationId, Throwable cause) {
        super(message, cause);
        this.linkName = linkName;
        this.correlationId = correlationId;
    }
}
