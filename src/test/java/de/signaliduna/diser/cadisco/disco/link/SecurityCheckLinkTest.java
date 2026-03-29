package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.disco.model.AMSInput;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class SecurityCheckLinkTest {

    private final SecurityCheckLink link = new SecurityCheckLink();

    @Test
    void admitsKvGuest() {
        DiscoContext ctx = new DiscoContext("G-001");
        AMSInput guest = new AMSInput("G-001", "Max", "KV", List.of("V-1"));

        StepVerifier.create(link.process(Mono.just(guest), ctx))
                .expectNextMatches(g -> "G-001".equals(g.getGlobalId()))
                .verifyComplete();
    }

    @Test
    void rejectsNonKvGuest() {
        DiscoContext ctx = new DiscoContext("G-002");
        AMSInput guest = new AMSInput("G-002", "Erika", "LV", List.of("V-2"));

        StepVerifier.create(link.process(Mono.just(guest), ctx))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException
                        && e.getMessage().contains("NICHT_ZUSTÄNDIG"))
                .verify();
    }
}
