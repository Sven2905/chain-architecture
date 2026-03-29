package de.signaliduna.diser.cadisco.core;

public interface ChainContext {

    void log(String msg);

    String getCorrelationId();

    String getTraceId();
}
