package de.signaliduna.diser.cadisco.core;

import reactor.core.publisher.Mono;

/**
 * Ein einzelnes Glied in der Verarbeitungskette.
 *
 * <p>Der {@code ChainLink} ist die kleinste Einheit der Geschaeftslogik. Jedes Glied
 * nimmt einen reaktiven Input entgegen, transformiert ihn, und reicht das Ergebnis
 * an das naechste Glied weiter. Einfach, elegant, und -- wenn man es richtig macht --
 * voellig unabhaengig von seinen Nachbarn.</p>
 *
 * <p><strong>Link-Isolation:</strong> Ein Link darf niemals wissen, was zwei Glieder
 * weiter passiert. Wer spickt, fliegt.</p>
 *
 * @param <I> der Eingabetyp -- was hereinkommt
 * @param <O> der Ausgabetyp -- was herauskommt (idealerweise besser als vorher)
 * @param <C> der konkrete Context-Typ, mindestens ein {@link ChainContext}
 */
public interface ChainLink<I, O, C extends ChainContext> {

    /**
     * Fuehrt die reaktive Transformation dieses Links durch.
     *
     * <p>Die Methode erhaelt den Input als {@link Mono} und den geteilten Context.
     * Sie darf den Context modifizieren, sollte aber den Input als immutable behandeln.</p>
     *
     * @param input   der reaktive Eingabestrom
     * @param context der geteilte Verarbeitungskontext
     * @return ein {@link Mono} mit dem transformierten Ergebnis
     */
    Mono<O> transform(Mono<I> input, C context);
}
