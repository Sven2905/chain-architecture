package de.signaliduna.diser.cadisco.api;

import de.signaliduna.diser.cadisco.core.Chain;
import de.signaliduna.diser.cadisco.core.ChainContext;
import de.signaliduna.diser.cadisco.core.DefaultChainContext;
import de.signaliduna.diser.cadisco.demo.DecorationLink;
import de.signaliduna.diser.cadisco.demo.UppercaseLink;
import de.signaliduna.diser.cadisco.demo.ValidationLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Demo-Controller fuer die Chain Architecture.
 * Zeigt die synchrone Kette mit einfachen String-Transformationen.
 */
@RestController
@RequestMapping("/api/chain")
public class ChainController {

    @GetMapping("/test")
    public Mono<String> testChain(@RequestParam(defaultValue = "  Hello Chain Architecture  ") String input) {
        ChainContext ctx = new DefaultChainContext();
        ctx.log("Starting test-chain via REST...");

        String result = Chain.start(input, ctx)
                .link(new ValidationLink())
                .link(new UppercaseLink())
                .link(new DecorationLink())
                .execute();

        return Mono.just(result);
    }
}
