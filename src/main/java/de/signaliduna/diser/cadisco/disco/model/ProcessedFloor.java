package de.signaliduna.diser.cadisco.disco.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ergebnis der Verarbeitung auf einem spezifischen Floor.
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

    public static ProcessedFloor success(FloorDefinition floor, String data) {
        return ProcessedFloor.builder().definition(floor).extractedData(data).successful(true).build();
    }

    public static ProcessedFloor failure(FloorDefinition floor, String error) {
        return ProcessedFloor.builder().definition(floor).successful(false).errorMessage(error).build();
    }
}
