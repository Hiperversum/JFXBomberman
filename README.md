# JFXBomberman

Gioco in stile Bomberman scritto in **Java + JavaFX** (progetto del 2023), con menu utenti/statistiche, livelli, nemici (NPC), power-up e un **editor di livelli e mondi** integrato.

Architettura MVC: `src/controller`, `src/model`, `src/view`; asset (sprite, audio, font, FXML, CSS, livelli) in `res/`.

## Requisiti

- **JDK 17 o superiore** (testato con JDK 23)
- Nient'altro: Maven e JavaFX vengono scaricati automaticamente dal Maven Wrapper incluso.

## Avvio

Dalla cartella del progetto:

```bash
# Windows
mvnw.cmd javafx:run

# Linux / macOS
./mvnw javafx:run
```

> Il gioco legge gli asset dalla cartella `res/` relativa alla directory di lavoro, quindi va avviato dalla root del progetto.

## Compilazione

```bash
./mvnw compile
```

## Struttura

| Cartella | Contenuto |
|---|---|
| `src/controller` | Entry point (`controller.JBomberMan`) e controller FXML |
| `src/model` | Logica di gioco, engine, editor, gestione utenti e risorse |
| `src/view` | Viste e componenti JavaFX |
| `res/` | Sprite, audio, musica, font, FXML, CSS, livelli/mondi di default |
| `res/users/` | Profili utente creati a runtime (non versionati) |
