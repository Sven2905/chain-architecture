package de.signaliduna.diser.cadisco.demo;

import de.signaliduna.diser.cadisco.core.ChainContext;
import de.signaliduna.diser.cadisco.core.ChainLink;

/**
 * Demo-Link zur Validierung von Strings.
 */
public class ValidationLink implements ChainLink<String, String, ChainContext> {

    @Override
    public String process(String input, ChainContext ctx) {
        ctx.log("Validating input: '" + input + "'");
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input must not be empty or blank");
        }
        return input.trim();
    }
}
