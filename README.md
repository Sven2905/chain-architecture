# Ca-Disco: Chain Architecture & Disco Application

> *"Manchmal muss man eine ganze Architektur bauen, nur um in die Disco zu kommen."*
> -- J.A.R.V.I.S., vermutlich

Willkommen beim **Ca-Disco** Projekt. Eine Referenz-Implementierung der **Chain Architecture** -- einem Chain-of-Responsibility Framework, das beweist, dass sequenzielle Integrität eleganter ist als hexagonale Komplexität. Gebaut mit einer Bescheidenheit, die der Qualität des Codes angemessen ist: gar keine.

## Das Konzept

Die Chain ist **synchron und dumm** -- im besten Sinne. Sie reicht Werte durch, mehr nicht. Ob ein Link intern mit `Mono`, `String` oder Kartoffeln arbeitet, ist seine Sache. Die Reaktivität lebt in den Links, nicht im Framework.

```java
// Synchron -- simpel, lesbar, fertig
Chain.start(input, ctx)
    .link(new ValidationLink())
    .link(new UppercaseLink())
    .link(new DecorationLink())
    .execute();

// Reaktiv -- Mono als Typ-Parameter, nicht als Framework-Feature
Chain.start(Mono.just(request), ctx)
    .link(new SecurityCheckLink())        // Mono<AMSInput> → Mono<AMSInput>
    .link(new GuestDataSyncLink(...))     // Mono<AMSInput> → Mono<AMSInput>
    .link(new FloorSelectorLink(...))     // Mono<AMSInput> → Mono<List<FloorDefinition>>
    .execute();
```

`ChainLink` ist ein `@FunctionalInterface` -- Lambdas willkommen.

## Projekt-Struktur

```
src/main/java/de/signaliduna/diser/cadisco/
├── CadiscoApplication.java
├── api/
│   └── ChainController.java              # Demo-Endpunkt: GET /api/chain/test
├── core/
│   ├── Chain.java                         # Der Orchestrator. Synchron. Schlank.
│   ├── ChainContext.java                  # Interface: traceId + log()
│   ├── ChainLink.java                     # @FunctionalInterface: I → O
│   └── DefaultChainContext.java           # Standard-Implementierung
├── demo/
│   ├── ValidationLink.java               # String-Validierung
│   ├── UppercaseLink.java                # Grossbuchstaben
│   └── DecorationLink.java              # Dekoration
└── disco/
    ├── config/
    │   └── DiscoConfigProperties.java     # Floors aus YAML laden
    ├── controller/
    │   └── DiscoEntranceController.java   # POST /disco/entry
    ├── links/
    │   ├── SecurityCheckLink.java         # Bouncer
    │   ├── GuestDataSyncLink.java         # Archiv+ & Icdos mit Retry
    │   ├── FloorSelectorLink.java         # DJ waehlt Floors
    │   ├── FloorRequirementLink.java      # Dresscode pruefen
    │   ├── FloorForwardingLink.java       # Ab auf die Tanzflaeche
    │   └── DiscoReportLink.java           # Abschlussbericht
    ├── model/
    │   ├── AMSInput.java                  # Gast-Input vom AMS
    │   ├── DiscoContext.java              # Zustand der Nacht
    │   ├── DiscoResponse.java             # Antwort mit State-Enum
    │   ├── FloorDefinition.java           # Floor-Konfiguration
    │   └── ProcessedFloor.java            # Verarbeitungsergebnis
    └── service/
        ├── ArchivPlusService.java         # Dokumente mit Polling & Fallback
        ├── DjService.java                 # Floor-Logik, config-driven
        └── IcdosService.java              # Vertragsdetails
src/main/resources/
├── application.yml
└── floors/                                # Modulare Floor-Definitionen
    ├── adressen_floor.yml
    └── bank_floor.yml
```

## Technische Voraussetzungen

| Komponente | Version | Kommentar |
|---|---|---|
| Java | 25 | Automatisch via Gradle Toolchain provisioniert |
| Spring Boot | 4.0.5 | WebFlux |
| Gradle | 8.14+ | Wrapper liegt bei |
| Lombok | latest | Weniger tippen, mehr denken |

## Schnellstart

```bash
./gradlew bootRun
```

### Endpoints

**Demo-Chain (synchron):**
```bash
curl "http://localhost:8080/api/chain/test?input=hello%20world"
# >>> HELLO WORLD <<<
```

**Disco-Einlass:**
```bash
curl -X POST http://localhost:8080/disco/entry \
  -H "Content-Type: application/json" \
  -d '{"globalId": "G-001", "name": "Max Mustermann", "sparte": "KV", "contractNumbers": ["V-123"]}'
```
```json
{"globalId":"G-001","state":"IN_BEARBEITUNG"}
```

**Abgewiesener Gast (falsche Sparte):**
```bash
curl -X POST http://localhost:8080/disco/entry \
  -H "Content-Type: application/json" \
  -d '{"globalId": "G-002", "name": "Erika Musterfrau", "sparte": "LV", "contractNumbers": ["V-456"]}'
```
```json
{"globalId":"G-002","state":"NICHT_ZUSTÄNDIG"}
```

## Architektur-Prinzipien

- **Chain ist synchron**: Sie reicht Werte durch. Reaktivität ist Implementierungsdetail der Links.
- **@FunctionalInterface**: Links koennen als Lambdas geschrieben werden.
- **Fail-Fast**: Validierung am Anfang der Kette. Wer nicht reinkommt, fuer den wird kein Floor reserviert.
- **Link-Isolation**: Ein Link kennt nur Input und Output. Kein Spicken.
- **Config-driven Floors**: Neue Floors als YAML anlegen, automatisch geladen.
- **Lineare Debuggability**: Stacktraces sind immer linear. Trivial zu lesen.

---

*Gebaut von einem Butler, der Code schreibt wie er Tee serviert: mit Praezision, einer Prise Arroganz, und dem unerschuetterlichen Glauben, dass es niemand besser koennte.*
