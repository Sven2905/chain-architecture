package de.signaliduna.diser.cadisco.core;

import de.signaliduna.diser.cadisco.demo.ValidationLink;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChainTest {

    @Test
    void testChainFlow() {
        ChainContext ctx = new DefaultChainContext();

        String result = Chain.start("  hello  ", ctx)
                .link(new ValidationLink())
                .link((data, c) -> data.toUpperCase())
                .execute();

        assertEquals("HELLO", result);
    }

    @Test
    void testChainWithReactiveLambda() {
        ChainContext ctx = new DefaultChainContext();

        Mono<String> chainResult = Chain.start("  hello  ", ctx)
                .link(new ValidationLink())
                .link((data, c) -> Mono.just(data.toUpperCase()))
                .execute();

        StepVerifier.create(chainResult)
                .expectNext("HELLO")
                .verifyComplete();
    }

    @Test
    void testChainBreakOnValidationError() {
        ChainContext ctx = new DefaultChainContext();

        assertThrows(IllegalArgumentException.class, () ->
                Chain.start(" ", ctx)
                        .link(new ValidationLink())
                        .link((data, c) -> "SHOULD NOT HAPPEN")
                        .execute()
        );
    }
}
