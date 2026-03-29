package de.signaliduna.diser.cadisco.core;

/**
 * Ein Glied in der Kette.
 *
 * <p>{@code @FunctionalInterface} -- kann als Lambda verwendet werden.
 * Die Chain reicht Werte synchron durch; ob der Link intern mit
 * {@code Mono}, {@code String} oder Kartoffeln arbeitet, ist seine Sache.</p>
 *
 * @param <I> Input-Typ
 * @param <O> Output-Typ
 * @param <C> Kontext-Typ
 */
@FunctionalInterface
public interface ChainLink<I, O, C extends ChainContext> {

    O process(I input, C ctx);
}
