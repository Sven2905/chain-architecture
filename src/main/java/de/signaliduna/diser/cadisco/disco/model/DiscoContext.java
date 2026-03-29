package de.signaliduna.diser.cadisco.disco.model;

import de.signaliduna.diser.cadisco.core.ChainContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Der Kontext fuer die Verarbeitung in der Disco.
 * Speichert alle relevanten Infos ueber den Gast und die Ergebnisse seiner Tanz-Besuche.
 */
@Slf4j
@Getter
public class DiscoContext implements ChainContext {

    private final UUID traceId = UUID.randomUUID();
    private final String globalId;
    private final List<String> logs = new ArrayList<>();
    private final List<String> downloadedDocuments = new ArrayList<>();
    private final List<String> retrievedContracts = new ArrayList<>();
    private final List<FloorDefinition> availableFloors = new ArrayList<>();
    private final List<ProcessedFloor> results = new ArrayList<>();

    public DiscoContext(String globalId) {
        this.globalId = globalId;
    }

    @Override
    public void log(String message) {
        String entry = String.format("[%s] [%s] %s", LocalDateTime.now(), globalId, message);
        logs.add(entry);
        log.info(entry);
    }

    public List<String> getFullLog() {
        return Collections.unmodifiableList(logs);
    }

    public void addDocument(String doc) {
        downloadedDocuments.add(doc);
    }

    public void addContract(String contract) {
        retrievedContracts.add(contract);
    }

    public void setAvailableFloors(List<FloorDefinition> floors) {
        this.availableFloors.clear();
        this.availableFloors.addAll(floors);
    }

    public void addResult(ProcessedFloor result) {
        results.add(result);
    }
}
