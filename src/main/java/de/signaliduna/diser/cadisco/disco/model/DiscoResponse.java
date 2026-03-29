package de.signaliduna.diser.cadisco.disco.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Die Antwort der Disco am Einlass.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscoResponse {

    private String globalId;
    private State state;

    public enum State {
        IN_BEARBEITUNG,
        VERARBEITET,
        TEILWEISE_VERARBEITET,
        NICHT_ZUSTÄNDIG
    }
}
