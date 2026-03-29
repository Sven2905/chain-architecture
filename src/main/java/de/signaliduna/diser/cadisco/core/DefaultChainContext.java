package de.signaliduna.diser.cadisco.core;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Standard-Implementierung des {@link ChainContext}.
 * Generiert eine Trace-ID und loggt via SLF4J.
 */
@Slf4j
public class DefaultChainContext implements ChainContext {

    private final UUID traceId;

    public DefaultChainContext() {
        this.traceId = UUID.randomUUID();
    }

    @Override
    public UUID getTraceId() {
        return traceId;
    }

    @Override
    public void log(String message) {
        log.info("[{}] {}", traceId, message);
    }
}
