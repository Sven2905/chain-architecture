package de.signaliduna.diser.cadisco.disco.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Der Archiv+ Service fuer den Zugriff auf Dokumente.
 */
@Slf4j
@Service
public class ArchivPlusService {

    public Mono<String> requestDocuments(String globalId) {
        log.info("Archiv+: Receiving document request for guest {}", globalId);
        return Mono.just("DOC_REQ_" + UUID.randomUUID());
    }

    public Mono<String> getArchiveStatus(String requestId) {
        return Mono.just("COMPLETED");
    }

    public Mono<String> getDownloadLink(String globalId) {
        return Mono.just("https://archivplus.internal/download/" + globalId + ".pdf");
    }

    public Mono<String> fallbackDocuments() {
        return Mono.just("FALLBACK_DOC_PDF");
    }
}
