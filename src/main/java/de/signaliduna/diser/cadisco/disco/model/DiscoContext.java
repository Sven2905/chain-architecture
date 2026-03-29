package de.signaliduna.diser.cadisco.disco.model;

import de.signaliduna.diser.cadisco.core.DefaultChainContext;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Der Kontext fuer die Verarbeitung in der Disco.
 * Erweitert {@link DefaultChainContext} um disco-spezifischen Zustand.
 *
 * <p>Thread-safe: Alle Listen verwenden {@link CopyOnWriteArrayList} fuer parallele Mono-Operationen.
 * Hinweis: {@link #setAvailableFloors} darf nur vor Chain-Start aufgerufen werden (nicht atomar).</p>
 */
@Getter
public class DiscoContext extends DefaultChainContext {

    private final String globalId;
    private final List<String> logs = new CopyOnWriteArrayList<>();
    private final List<String> downloadedDocuments = new CopyOnWriteArrayList<>();
    private final List<String> retrievedContracts = new CopyOnWriteArrayList<>();
    private final List<FloorDefinition> availableFloors = new CopyOnWriteArrayList<>();

    public DiscoContext(String globalId) {
        this.globalId = globalId;
    }

    @Override
    public void log(String message) {
        String entry = String.format("[%s] [%s] %s", LocalDateTime.now(), globalId, message);
        logs.add(entry);
        super.log(message);
    }

    public void addDocument(String doc) {
        downloadedDocuments.add(doc);
    }

    public void addContract(String contract) {
        retrievedContracts.add(contract);
    }

    /**
     * Setzt die verfuegbaren Floors. Nur vor Chain-Start aufrufen (nicht atomar).
     */
    public void setAvailableFloors(List<FloorDefinition> floors) {
        availableFloors.clear();
        availableFloors.addAll(floors);
    }
}
