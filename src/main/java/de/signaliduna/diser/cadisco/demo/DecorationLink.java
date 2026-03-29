package de.signaliduna.diser.cadisco.demo;

import de.signaliduna.diser.cadisco.core.ChainContext;
import de.signaliduna.diser.cadisco.core.ChainLink;

/**
 * Demo-Link zur dekorativen Verzierung von Strings.
 */
public class DecorationLink implements ChainLink<String, String, ChainContext> {

    @Override
    public String process(String input, ChainContext ctx) {
        ctx.log("Wrapping '" + input + "' in decorators.");
        return ">>> " + input + " <<<";
    }
}
