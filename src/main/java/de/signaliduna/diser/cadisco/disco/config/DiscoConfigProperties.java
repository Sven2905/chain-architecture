package de.signaliduna.diser.cadisco.disco.config;

import de.signaliduna.diser.cadisco.disco.model.FloorDefinition;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Konfiguration fuer Disco Floors, geladen aus modularen YAML-Dateien.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "disco")
public class DiscoConfigProperties {

    private Map<String, FloorDefinition> floors;

    public List<FloorDefinition> getAsList() {
        if (floors == null) {
            return List.of();
        }
        return new ArrayList<>(floors.values());
    }
}
