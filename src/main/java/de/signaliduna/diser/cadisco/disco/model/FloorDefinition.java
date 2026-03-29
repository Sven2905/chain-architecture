package de.signaliduna.diser.cadisco.disco.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Definition eines Dancefloors in der Disco.
 * Entspricht fachlich einem Kundenanliegen.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloorDefinition {

    private String name;
    private String useWhen;
    private String url;
    private String requiredData;
}
