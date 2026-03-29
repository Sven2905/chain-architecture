package de.signaliduna.diser.cadisco.disco.link;

import de.signaliduna.diser.cadisco.core.ChainLink;
import de.signaliduna.diser.cadisco.disco.context.DiscoContext;
import de.signaliduna.diser.cadisco.disco.dto.DiscoReport;
import de.signaliduna.diser.cadisco.disco.dto.GuestData;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Erstellt den Abschlussbericht der Disco-Nacht -- letztes Glied der Kette.
 *
 * <p>Sammelt alle relevanten Daten aus dem {@link DiscoContext} und verpackt
 * sie in einen immutablen {@link DiscoReport}. Ein wuerdiger Abschluss fuer
 * eine gut orchestrierte Nacht.</p>
 *
 * <p>Der Bericht enthaelt: besuchte Floors, das vollstaendige Verlaufsprotokoll,
 * und einen Zeitstempel. Alles, was man fuer die Nachbesprechung braucht --
 * oder fuer die Ausrede am naechsten Morgen.</p>
 */
public class DiscoReportLink implements ChainLink<GuestData, DiscoReport, DiscoContext> {

    @Override
    public Mono<DiscoReport> transform(Mono<GuestData> input, DiscoContext context) {
        return input.map(guestData -> {
            context.log("Abschlussbericht wird erstellt für Gast " + guestData.guestId() + ".");
            DiscoReport report = new DiscoReport(
                    guestData.guestId(),
                    new ArrayList<>(context.getVisitedFloors()),
                    new ArrayList<>(context.getLogs()),
                    Instant.now()
            );
            context.log("Disco-Nacht beendet. Besuchte Floors: " + context.getVisitedFloors());
            return report;
        });
    }
}
