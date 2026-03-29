# Ca-Disco: Reactive Chain Architecture & Disco Application

> *"Manchmal muss man eine ganze Architektur bauen, nur um in die Disco zu kommen."*
> -- J.A.R.V.I.S., vermutlich

Willkommen beim **Ca-Disco** Projekt. Eine Referenz-Implementierung der **Chain Architecture** -- einem reaktiven Chain-of-Responsibility Framework, das beweist, dass sequenzielle Integrität eleganter ist als hexagonale Komplexität. Gebaut mit einer Bescheidenheit, die der Qualität des Codes angemessen ist: gar keine.

## Was ist das hier?

Zwei Dinge, Sir:

### 1. Chain Architecture (Core)
Ein generisches, reaktives Framework zur Orchestrierung von Logik-Ketten. Typsicher, non-blocking, und mit einer Fluent-API, die sich liest wie ein guter Roman -- nur mit mehr spitzen Klammern.

```java
Chain.start(Mono.just(inputData), context)
    .link(new ValidationLink())
    .link(new ProcessingLink())
    .link(new PersistenceLink())
    .execute()
    .subscribe();
```

### 2. Disco Application (Implementation)
Eine fachliche Demonstration, die den Prozess eines Disco-Besuchs simuliert. Weil nichts eine Architektur besser demonstriert als ein Bouncer, der Gäste abweist.

**Phase 1 -- Der Eingang** (synchron): Security-Check. Rein oder nicht rein. Wie im echten Leben, nur mit weniger Diskussion.

**Phase 2 -- Die Nacht** (asynchron): Datensynchronisation, Floor-Auswahl, Anforderungsprüfung, Weiterleitung und Abschlussbericht. Alles fire-and-forget im Hintergrund -- die HTTP-Response ist längst zurück, während die Disco-Nacht gerade erst beginnt.

## Projekt-Struktur

```
src/main/java/de/signaliduna/diser/cadisco/
├── CadiscoApplication.java
├── core/                          # Das Framework
│   ├── Chain.java                 # Der Orchestrator. Das Gehirn.
│   ├── ChainContext.java          # Geteilter Zustand. Das Gedächtnis.
│   ├── ChainLink.java             # Ein Glied der Kette. Ein Arbeiter.
│   └── ChainLinkException.java    # Wenn ein Glied reißt. Passiert den Besten.
└── disco/                         # Die Anwendung
    ├── context/
    │   └── DiscoContext.java      # Disco-spezifischer Zustand
    ├── controller/
    │   └── DiscoEntranceController.java  # POST /disco/entry
    ├── dto/                       # Immutable Records. Sauber. Unveränderlich. Vorbildlich.
    │   ├── DiscoEntryRequest.java
    │   ├── DiscoEntryResponse.java
    │   ├── DiscoReport.java
    │   ├── FloorInfo.java
    │   └── GuestData.java
    ├── link/                      # Die Glieder der Kette
    │   ├── SecurityCheckLink.java
    │   ├── GuestDataSyncLink.java
    │   ├── FloorSelectorLink.java
    │   ├── FloorRequirementLink.java
    │   ├── FloorForwardingLink.java
    │   └── DiscoReportLink.java
    └── service/                   # Legacy-System-Simulationen
        ├── DjService.java
        ├── ArchivPlusService.java
        └── IcdosService.java
```

## Technische Voraussetzungen

| Komponente | Version | Kommentar |
|---|---|---|
| Java | 25 | Wird automatisch via Gradle Toolchain provisioniert |
| Spring Boot | 4.0.5 | WebFlux, non-blocking |
| Gradle | 8.14+ | Wrapper liegt bei |
| Lombok | latest | Weniger tippen, mehr denken |

## Schnellstart

```bash
./gradlew bootRun
```

Keine Java-25-Installation nötig -- Gradle regelt das. Die Disco ist dann unter `http://localhost:8080/disco/entry` erreichbar.

### Beispiel-Requests

**Gast wird eingelassen:**
```bash
curl -X POST http://localhost:8080/disco/entry \
  -H "Content-Type: application/json" \
  -d '{"guestId": "G-001", "region": "NRW"}'
```
```json
{"guestId":"G-001","status":"ADMITTED","message":"Willkommen in der Disco, G-001!"}
```

**Gast wird abgewiesen (gesperrte Region):**
```bash
curl -X POST http://localhost:8080/disco/entry \
  -H "Content-Type: application/json" \
  -d '{"guestId": "G-002", "region": "BLOCKED"}'
```
```json
{"guestId":"G-002","status":"DENIED","message":"Region 'BLOCKED' ist gesperrt."}
```

## Architektur-Prinzipien

- **Broken Link Principle**: Reißt ein Glied, stoppt die Kette. Zentrale Fehlerbehandlung im Anchor.
- **Fail-Fast**: Validierung immer am Anfang. Keine halben Sachen.
- **Link-Isolation**: Ein Link kennt nur seinen Input und Output. Was drei Glieder weiter passiert, geht ihn nichts an.
- **Immutabilität**: Daten fließen als Records durch die Kette. Der Context ist der einzige, der sich Veränderung erlauben darf.
- **Lineare Debuggability**: Stacktraces in der Chain Architecture sind immer linear. Trivial zu lesen, trivial zu fixen.

---

*Gebaut von einem Butler, der Code schreibt wie er Tee serviert: mit Präzision, einer Prise Arroganz, und dem unerschütterlichen Glauben, dass es niemand besser könnte.*
