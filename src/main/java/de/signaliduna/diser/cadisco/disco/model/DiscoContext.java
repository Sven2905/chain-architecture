package de.signaliduna.diser.cadisco.disco.model;

import de.signaliduna.diser.cadisco.core.DefaultChainContext;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Der Kontext fuer die Verarbeitung in der Disco.
 * Erweitert {@link DefaultChainContext} um disco-spezifischen Zustand.
 * Thread-safe: Alle Listen verwenden CopyOnWriteArrayList fuer parallele Mono-Operationen.
 */
@Getter
public class DiscoContext extends DefaultChainContext {

    private final String globalId;
    private final List<String> logs = new CopyOnWriteArrayList<>();
    private final List<String> downloadedDocuments = new CopyOnWriteArrayList<>();
    private final List<String> retrievedContracts = new CopyOnWriteArrayList<>();
    private final List<FloorDefinition> availableFloors = new CopyOnWriteArrayList<>();
    private final List<ProcessedFloor> results = new CopyOnWriteArrayList<>();

    public DiscoContext(String globalId) {
        this.globalId = globalId;
    }

    @Override
    public void log(String message) {
        String entry = String.format("[%s] [%s] %s", LocalDateTime.now(), globalId, message);
        logs.add(entry);
        super.log(message);
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
        availableFloors.clear();
        availableFloors.addAll(floors);
    }

    public void addResult(ProcessedFloor result) {
        results.add(result);
    }
}
