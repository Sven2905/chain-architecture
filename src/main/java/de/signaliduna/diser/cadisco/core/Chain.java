package de.signaliduna.diser.cadisco.core;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Chain<I, C extends ChainContext> {

    private final Mono<I> pipeline;
    private final C context;

    private Chain(Mono<I> pipeline, C context) {
        this.pipeline = pipeline;
        this.context = context;
    }

    public static <I, C extends ChainContext> Chain<I, C> start(Mono<I> input, C context) {
        context.log("Chain gestartet [correlationId=" + context.getCorrelationId() + "]");
        return new Chain<>(input, context);
    }

    public <O> Chain<O, C> link(ChainLink<I, O, C> link) {
        String linkName = link.getClass().getSimpleName();
        Mono<O> next = pipeline
                .doOnSubscribe(s -> context.log("Link '" + linkName + "' wird ausgeführt..."))
                .flatMap(value -> link.transform(Mono.just(value), context))
                .doOnSuccess(result -> context.log("Link '" + linkName + "' erfolgreich abgeschlossen."))
                .doOnError(error -> context.log("Link '" + linkName + "' fehlgeschlagen: " + error.getMessage()));
        return new Chain<>(next, context);
    }

    public Mono<I> execute() {
        return pipeline
                .doOnSubscribe(s -> log.info("[{}] Chain-Ausführung gestartet.", context.getCorrelationId()))
                .doOnSuccess(result -> log.info("[{}] Chain-Ausführung erfolgreich abgeschlossen.", context.getCorrelationId()))
                .doOnError(error -> log.error("[{}] Chain-Ausführung fehlgeschlagen: {}", context.getCorrelationId(), error.getMessage()));
    }
}
