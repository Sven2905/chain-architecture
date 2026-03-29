package de.signaliduna.diser.cadisco.core;

/**
 * Orchestrator der Chain Architecture.
 *
 * <p>Ermoeglicht das fluide Aneinanderreihen von {@link ChainLink Links}.
 * Die Chain selbst ist synchron -- sie reicht Werte durch. Reaktivitaet
 * ist Sache der Links, nicht des Frameworks. Einfach, typsicher, lesbar.</p>
 *
 * <pre>{@code
 * Chain.start(input, ctx)
 *     .link(new ValidationLink())
 *     .link(new ProcessingLink())
 *     .execute();
 * }</pre>
 *
 * @param <T> der aktuelle Typ des Wertes in der Kette
 * @param <C> der Typ des Kontextes
 */
public class Chain<T, C extends ChainContext> {

    private final T value;
    private final C context;

    private Chain(T value, C context) {
        this.value = value;
        this.context = context;
    }

    /**
     * Startet eine neue Kette mit einem initialen Wert.
     */
    public static <I, CX extends ChainContext> Chain<I, CX> start(I input, CX context) {
        return new Chain<>(input, context);
    }

    /**
     * Fuegt ein neues Glied zur Kette hinzu.
     */
    public <O> Chain<O, C> link(ChainLink<T, O, C> link) {
        return new Chain<>(link.process(value, context), context);
    }

    /**
     * Gibt das finale Ergebnis der Kette zurueck.
     */
    public T execute() {
        return value;
    }
}
