package de.signaliduna.diser.cadisco.core;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Der Orchestrator -- das Gehirn der Chain Architecture.
 *
 * <p>Die {@code Chain} verbindet einzelne {@link ChainLink Links} zu einer reaktiven
 * Verarbeitungspipeline. Intern wird jeder {@code .link()}-Aufruf per {@code flatMap}
 * in die bestehende {@link Mono}-Pipeline komponiert. Nichts wird ausgefuehrt, bis
 * jemand {@code .execute()} aufruft und subscribt -- lazy evaluation in Perfektion.</p>
 *
 * <h3>Benutzung:</h3>
 * <pre>{@code
 * Chain.start(Mono.just(input), context)
 *     .link(new ValidationLink())
 *     .link(new ProcessingLink())
 *     .link(new PersistenceLink())
 *     .execute()
 *     .subscribe();
 * }</pre>
 *
 * <p>Jeder Link-Aufruf gibt eine neue {@code Chain}-Instanz zurueck, parametrisiert
 * auf den Output-Typ des hinzugefuegten Links. Der Java-Compiler stellt sicher,
 * dass benachbarte Links typkompatibel sind -- Fehler zur Compile-Zeit statt zur
 * Laufzeit. So gehoert sich das.</p>
 *
 * @param <I> der aktuelle Typ in der Pipeline
 * @param <C> der konkrete Context-Typ
 */
@Slf4j
public class Chain<I, C extends ChainContext> {

    private final Mono<I> pipeline;
    private final C context;

    private Chain(Mono<I> pipeline, C context) {
        this.pipeline = pipeline;
        this.context = context;
    }

    /**
     * Erzeugt eine neue Chain mit dem gegebenen Input und Context.
     *
     * <p>Dies ist der Startpunkt jeder Kette. Der Context wird fuer die gesamte
     * Lebensdauer der Chain durchgereicht.</p>
     *
     * @param input   der initiale Input als reaktiver Stream
     * @param context der geteilte Verarbeitungskontext
     * @param <I>     der Eingabetyp
     * @param <C>     der Context-Typ
     * @return eine neue Chain, bereit fuer {@code .link()}-Aufrufe
     */
    public static <I, C extends ChainContext> Chain<I, C> start(Mono<I> input, C context) {
        context.log("Chain gestartet [correlationId=" + context.getCorrelationId() + "]");
        return new Chain<>(input, context);
    }

    /**
     * Fuegt ein neues Glied an die Kette an.
     *
     * <p>Intern wird die Transformation des Links via {@code flatMap} in die bestehende
     * Pipeline komponiert. Logging-Callbacks ({@code doOnSubscribe}, {@code doOnSuccess},
     * {@code doOnError}) werden automatisch dekoriert -- man muss sich also nicht selbst
     * um Observability kuemmern. Das waere auch unter meiner Wuerde.</p>
     *
     * @param link der anzufuegende Link
     * @param <O>  der Output-Typ des Links (wird zum neuen Pipeline-Typ)
     * @return eine neue Chain, parametrisiert auf den Output-Typ des Links
     */
    public <O> Chain<O, C> link(ChainLink<I, O, C> link) {
        String linkName = link.getClass().getSimpleName();
        Mono<O> next = pipeline
                .doOnSubscribe(s -> context.log("Link '" + linkName + "' wird ausgeführt..."))
                .flatMap(value -> link.transform(Mono.just(value), context))
                .doOnSuccess(result -> context.log("Link '" + linkName + "' erfolgreich abgeschlossen."))
                .doOnError(error -> context.log("Link '" + linkName + "' fehlgeschlagen: " + error.getMessage()));
        return new Chain<>(next, context);
    }

    /**
     * Finalisiert die Chain und gibt die zusammengesetzte Pipeline zurueck.
     *
     * <p>Die zurueckgegebene {@link Mono} ist <em>lazy</em> -- erst ein {@code subscribe()}
     * startet die tatsaechliche Verarbeitung. In WebFlux-Controllern uebernimmt das Framework
     * die Subscription; fuer fire-and-forget Szenarien muss explizit subscribt werden.</p>
     *
     * @return die vollstaendig komponierte, reaktive Pipeline
     */
    public Mono<I> execute() {
        return pipeline
                .doOnSubscribe(s -> log.info("[{}] Chain-Ausführung gestartet.", context.getCorrelationId()))
                .doOnSuccess(result -> log.info("[{}] Chain-Ausführung erfolgreich abgeschlossen.", context.getCorrelationId()))
                .doOnError(error -> log.error("[{}] Chain-Ausführung fehlgeschlagen: {}", context.getCorrelationId(), error.getMessage()));
    }
}
