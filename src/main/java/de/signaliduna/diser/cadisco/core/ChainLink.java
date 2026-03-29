package de.signaliduna.diser.cadisco.core;

import reactor.core.publisher.Mono;

public interface ChainLink<I, O, C extends ChainContext> {

    Mono<O> transform(Mono<I> input, C context);
}
