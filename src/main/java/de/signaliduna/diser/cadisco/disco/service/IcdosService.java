package de.signaliduna.diser.cadisco.disco.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Der ICDOS-Service (externes System).
 * Liefert Vertragsdetails fuer die Disco-Prozesse.
 */
@Slf4j
@Service
public class IcdosService {

    public Mono<List<String>> getContracts(List<String> contractNumbers) {
        log.info("Icdos: Synchronizing details for {} contracts", contractNumbers.size());
        return Mono.just(List.of("CONTRACT-KV-123", "CONTRACT-Sach-456"));
    }
}
