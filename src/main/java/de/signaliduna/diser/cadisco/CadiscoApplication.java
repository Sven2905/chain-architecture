package de.signaliduna.diser.cadisco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Einstiegspunkt der Ca-Disco Anwendung.
 *
 * <p>Startet den reaktiven Spring-WebFlux-Server und damit die gesamte
 * Chain-Architecture-Infrastruktur. Danach steht die Disco unter
 * {@code POST /disco/entry} bereit -- vorausgesetzt, man besteht den Security-Check.</p>
 *
 * @author J.A.R.V.I.S. (mit minimaler menschlicher Aufsicht)
 */
@SpringBootApplication
public class CadiscoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CadiscoApplication.class, args);
    }
}
