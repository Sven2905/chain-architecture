package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.Chain;
import de.signaliduna.diser.cadisco.disco.model.AMSInput;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import de.signaliduna.diser.cadisco.disco.model.DiscoResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class DiscoChainIntegrationTest {

    @Test
    void phase1AdmitsKvGuestAndReturnsInBearbeitung() {
        DiscoContext ctx = new DiscoContext("G-001");
        AMSInput request = new AMSInput("G-001", "Max", "KV", List.of("V-1"));

        Mono<DiscoResponse> result = Chain.start(Mono.just(request), ctx)
                .link(new SecurityCheckLink())
                .execute()
                .map(guest -> new DiscoResponse(guest.getGlobalId(), DiscoResponse.State.IN_BEARBEITUNG));

        StepVerifier.create(result)
                .expectNextMatches(r -> "G-001".equals(r.getGlobalId())
                        && DiscoResponse.State.IN_BEARBEITUNG == r.getState())
                .verifyComplete();
    }

    @Test
    void phase1RejectsNonKvGuest() {
        DiscoContext ctx = new DiscoContext("G-002");
        AMSInput request = new AMSInput("G-002", "Erika", "LV", List.of("V-2"));

        Mono<DiscoResponse> result = Chain.start(Mono.just(request), ctx)
                .link(new SecurityCheckLink())
                .execute()
                .map(guest -> new DiscoResponse(guest.getGlobalId(), DiscoResponse.State.IN_BEARBEITUNG))
                .onErrorResume(e -> Mono.just(new DiscoResponse("G-002", DiscoResponse.State.NICHT_ZUSTÄNDIG)));

        StepVerifier.create(result)
                .expectNextMatches(r -> DiscoResponse.State.NICHT_ZUSTÄNDIG == r.getState())
                .verifyComplete();
    }
}
