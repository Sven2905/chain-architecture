package de.signaliduna.diser.cadisco.disco.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ein Ergebnis der Verarbeitung auf einem spezifischen Floor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedFloor {

    private FloorDefinition definition;
    private String extractedData;
    private boolean successful;
    private String errorMessage;
}
