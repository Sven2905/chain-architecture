package de.signaliduna.diser.cadisco.demo;

import de.signaliduna.diser.cadisco.core.ChainContext;
import de.signaliduna.diser.cadisco.core.ChainLink;

/**
 * Demo-Link zur Umwandlung in Grossbuchstaben.
 */
public class UppercaseLink implements ChainLink<String, String, ChainContext> {

    @Override
    public String process(String input, ChainContext ctx) {
        ctx.log("Transforming '" + input + "' to uppercase.");
        return input.toUpperCase();
    }
}
