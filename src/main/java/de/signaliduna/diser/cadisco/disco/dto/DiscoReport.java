package de.signaliduna.diser.cadisco.disco.dto;

import java.time.Instant;
import java.util.List;

public record DiscoReport(String guestId, List<String> visitedFloors, List<String> logs, Instant timestamp) {
}
