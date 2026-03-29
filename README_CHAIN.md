# Chain Architecture: Reaktives Chain-of-Responsibility Framework

Dieses Dokument beschreibt die Kernarchitektur des Projekts: Ein generisches Framework zur Orchestrierung von reaktiven Geschäftslogik-Ketten.

## 🔗 Das Konzept

Das **Chain Architecture Framework** basiert auf dem **Chain-of-Responsibility**-Entwurfsmuster. Es ermöglicht die Kapselung einzelner Logik-Schritte in überschaubare, reaktive "Links", die zu einer Kette verbunden werden.

### Hauptmerkmale:

- **100% Reaktiv**: Basiert auf **Project Reactor**, was eine effiziente, nicht-blockierende Verarbeitung ermöglicht.
- **Fluent-API**: Intuitive Orchestrierung durch Methodenverkettung (`Chain.start(...).link(...).execute()`).
- **Context-Driven**: Ein geteilter Zustand (`ChainContext`) wird durch die gesamte Kette gereicht.
- **Zustandskontrolle**: Jeder Link kann den Kontext modifizieren, was eine saubere Trennung von Datenfluss und Logik erlaubt.

---

## 🏛️ Kernkomponenten

### 1. `ChainContext`
Der Träger des Zustands während der Verarbeitung eines Requests. 
- Speichert Daten, die zwischen Links geteilt werden.
- Abstrahiert den Zugriff auf Request-Metadaten.

### 2. `ChainLink<I, C>`
Die kleinste Einheit der Verarbeitungslogik.
- **`I` (Input)**: Der Typ des Eingabedatensatzes (z.B. ein Request-DTO).
- **`C` (Context)**: Die konkrete Implementierung des `ChainContext`.
- Jeder Link implementiert die Methode `transform(Mono<I> input, C context)`, die eine reaktive Transformation durchführt.

### 3. `Chain<I, C>`
Der Orchestrator, der die Links zusammenhält.
- Bietet eine **Fluent-Builder-API**, um Links zu registrieren.
- Verwaltet den Input-Stream und stellt sicher, dass der Kontext korrekt durchgereicht wird.

---

## 🛠️ Benutzung (Beispiel für Java-Entwickler)

So einfach lässt sich eine Kette orchestrieren:

```java
Chain.start(Mono.just(inputData), context)
    .link(new ValidationLink())     // Erster Schritt: Validierung
    .link(new ProcessingLink())      // Zweiter Schritt: Logik
    .link(new NotificationLink())    // Dritter Schritt: Abschluss
    .execute()                       // Startet den reaktiven Stream
    .subscribe();                    // Asynchrone Ausführung
```

## 🚀 Warum Java 25 & WebFlux?

- **Non-blocking IO**: Skalierbarkeit durch WebFlux.
- **Type Safety**: Generische Implementierung gewährleistet Typsicherheit über die gesamte Kette hinweg.
- **Lombok Integration**: Minimale Boilerplate bei der Erstellung neuer Links.

Die Core-Komponenten finden sich im Package: `de.signaliduna.diser.cadisco.core`
