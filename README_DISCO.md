# Disco Application: Eine Referenz-Implementierung

Die **Disco Application** ist die fachliche Ausprägung der **Chain Architecture**. In diesem Projekt wird ein Disco-Szenario genutzt, um die Prinzipien des reaktiven Frameworks praktisch zu demonstrieren.

## 🕺 Die Fachlichkeit

Der Prozess eines Disco-Besuchs besteht aus zwei Phasen:

1. **Phase 1: Der Eingang (Bouncer/Security Check)**
   - Ein Gast kommt mit seiner ID und Region an.
   - Der Bouncer prüft, ob der Gast zugelassen wird.
   - Diese Phase wird **synchron** ausgeführt, da die Antwort für den Request entscheidet, ob der Gast eintreten darf.

2. **Phase 2: Die Nacht (Disco Night Flow)**
   - Wenn der Gast zugelassen wurde, startet die eigentliche "Nacht" **asynchron** im Hintergrund.
   - Es werden Daten synchronisiert, Floors berechnet und Berichte erstellt.

---

## 🏗️ Implementierung

### 1. `DiscoContext`
Erweitert `ChainContext` und speichert disco-spezifische Daten:
- Gast-ID, Name, Status.
- Verfügbare und gewählte Floors.
- Historisierte Logs über den Verlauf der Nacht.

### 2. `DiscoEntranceController`
Der REST-Endpunkt (`/disco/entry`), der die Orchestrierung startet. Hier wird das **Chain Framework** direkt in der Controller-Logik genutzt:

```java
// Phase 1 (Synchron)
return Chain.start(Mono.just(request), ctx)
        .link(new SecurityCheckLink())
        .execute()
        .map(guest -> {
            // Phase 2 (Asynchron im Hintergrund)
            startDiscoNight(Mono.just(guest), ctx);
            return new DiscoResponse(...);
        });
```

---

## 🔗 Die Links der Kette

Die Anwendung implementiert mehrere spezialisierte Links, die die Geschäftslogik abbilden:

- **`SecurityCheckLink`**: Verifiziert die Berechtigung (Region-Checks).
- **`GuestDataSyncLink`**: Synchronisiert Daten mit externen Services (**ArchivPlus**, **Icdos**).
- **`FloorSelectorLink`**: Nutzt den **DjService**, um passende Floors für den Gast zu finden.
- **`FloorRequirementLink`**: Überprüft Voraussetzungen (z.B. Mindestalter, VIP-Status).
- **`FloorForwardingLink`**: Simuliert die "Weiterleitung" auf die entsprechenden Tanzflächen.
- **`DiscoReportLink`**: Erzeugt den Abschlussbericht der Nacht.

## 🛠️ Services

Die Anwendung nutzt dedizierte Spring-Services zur Datenhaltung und Logik:
- **`DjService`**: Verwaltet Floors und BPM-Vorgaben.
- **`ArchivPlusService` & `IcdosService`**: Simulieren Anbindungen an Altsysteme.

Das Kernpaket der Anwendung ist: `de.signaliduna.diser.cadisco.disco`
