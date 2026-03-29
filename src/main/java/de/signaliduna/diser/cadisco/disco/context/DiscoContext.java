package de.signaliduna.diser.cadisco.disco.context;

import de.signaliduna.diser.cadisco.core.ChainContext;
import de.signaliduna.diser.cadisco.disco.dto.FloorInfo;
import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Der Disco-spezifische Verarbeitungskontext.
 *
 * <p>Erweitert den generischen {@link ChainContext} um alles, was man fuer einen
 * gepflegten Disco-Abend braucht: Gast-Daten, verfuegbare Floors, besuchte Floors,
 * und ein ausfuehrliches Protokoll der Nacht -- fuer den Fall, dass sich am naechsten
 * Morgen niemand mehr erinnert.</p>
 *
 * <p>Jede Instanz generiert automatisch eine Correlation-ID und Trace-ID.
 * Thread-Safety: Jeder HTTP-Request erzeugt seinen eigenen Context.
 * Geteilter Zustand zwischen Requests? Nicht in meinem Haus.</p>
 */
@Slf4j
@Getter
@Setter
public class DiscoContext implements ChainContext {

    private final String correlationId;
    private final String traceId;
    private final List<String> logs = new ArrayList<>();

    private String guestId;
    private String region;
    private String status;
    private GuestData guestData;
    private List<FloorInfo> availableFloors = new ArrayList<>();
    private List<FloorInfo> selectedFloors = new ArrayList<>();
    private List<String> visitedFloors = new ArrayList<>();

    public DiscoContext(String guestId, String region) {
        this.correlationId = UUID.randomUUID().toString().substring(0, 8);
        this.traceId = UUID.randomUUID().toString();
        this.guestId = guestId;
        this.region = region;
        this.status = "PENDING";
    }

    @Override
    public void log(String msg) {
        String entry = "[" + Instant.now() + "] [" + correlationId + "] " + msg;
        logs.add(entry);
        log.info(entry);
    }
}
