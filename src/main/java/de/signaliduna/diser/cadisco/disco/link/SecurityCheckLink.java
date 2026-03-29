package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.model.AMSInput;
import de.signaliduna.diser.cadisco.disco.model.DiscoContext;
import reactor.core.publisher.Mono;

/**
 * Der Tuersteher am Eingang der Disco.
 * Prueft den Dresscode (Sparte) des Gastes.
 */
public class SecurityCheckLink implements ChainLink<Mono<AMSInput>, Mono<AMSInput>, DiscoContext> {

    @Override
    public Mono<AMSInput> process(Mono<AMSInput> guestMono, DiscoContext ctx) {
        return guestMono.flatMap(guest -> {
            ctx.log("Bouncer: Checking dresscode for guest from region " + guest.getSparte());

            if (!"KV".equals(guest.getSparte())) {
                ctx.log("Bouncer: Sorry, KV-guests only tonight. Permission denied.");
                return Mono.error(new IllegalArgumentException("NICHT_ZUSTÄNDIG: Nur KV-Gäste haben heute Zutritt."));
            }

            ctx.log("Bouncer: Enjoy your night, " + guest.getName() + "! Welcome to the Disco.");
            return Mono.just(guest);
        });
    }
}
