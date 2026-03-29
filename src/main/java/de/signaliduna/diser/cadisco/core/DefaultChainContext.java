package de.signaliduna.diser.cadisco.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Standard-Implementierung des {@link ChainContext}.
 * Generiert eine Trace-ID und loggt via SLF4J.
 */
@Slf4j
@Getter
public class DefaultChainContext implements ChainContext {

    private final UUID traceId = UUID.randomUUID();

    @Override
    public void log(String message) {
        log.info("[{}] {}", traceId, message);
    }
}
