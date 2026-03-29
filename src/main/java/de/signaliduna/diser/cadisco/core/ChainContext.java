package de.signaliduna.diser.cadisco.core;

/**
 * Der Context Anchor -- das Gedaechtnis der Kette.
 *
 * <p>Jede Chain operiert auf einem geteilten Zustand, der durch die gesamte Kette
 * gereicht wird. Der {@code ChainContext} stellt die Querschnittsfunktionen bereit:
 * Logging, Correlation-ID fuer Tracing, und eine Trace-ID fuer verteilte Systeme.</p>
 *
 * <p>Konkrete Implementierungen erweitern dieses Interface um fachspezifische Felder.
 * Der Context ist bewusst <em>mutable</em> -- im Gegensatz zu den Daten, die als
 * immutable Records durch die Kette fliessen. Jemand muss sich schliesslich
 * die Dinge merken.</p>
 *
 * @see de.signaliduna.diser.cadisco.disco.context.DiscoContext
 */
public interface ChainContext {

    /**
     * Protokolliert eine Nachricht im Context-Log.
     * Jeder Eintrag wird automatisch mit Timestamp und Correlation-ID versehen.
     *
     * @param msg die zu protokollierende Nachricht
     */
    void log(String msg);

    /**
     * Liefert die Correlation-ID dieses Verarbeitungsvorgangs.
     * Kurz, knackig, eindeutig -- wie ein guter Witz.
     *
     * @return die Correlation-ID
     */
    String getCorrelationId();

    /**
     * Liefert die vollstaendige Trace-ID fuer verteiltes Tracing.
     *
     * @return die Trace-ID (UUID)
     */
    String getTraceId();
}
