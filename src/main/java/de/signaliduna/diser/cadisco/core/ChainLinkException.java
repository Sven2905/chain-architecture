package de.signaliduna.diser.cadisco.core;

import lombok.Getter;

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
