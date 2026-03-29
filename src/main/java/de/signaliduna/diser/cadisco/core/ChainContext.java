package de.signaliduna.diser.cadisco.core;

import java.util.UUID;

/**
 * Der "Anchor" der Chain Architecture.
 * Haelt den Kontext eines Prozessdurchlaufs (Trace-ID, Logging, Telemetrie).
 */
public interface ChainContext {

    UUID getTraceId();

    void log(String message);
}
