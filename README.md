# NewsApp - Android Case Study

Questa applicazione Android permette di consultare le ultime notizie tramite l'integrazione con le [NewsAPI](https://newsapi.org/), gestire una lista di preferiti locale e visualizzare i dettagli completi di ogni articolo.

## Configurazione e API Key

Per far funzionare l'applicazione, è necessario ottenere una API Key gratuita:

1. Registrati su [newsapi.org](https://newsapi.org/register).
2. Copia la tua **API Key**.
3. Inserisci la chiave nel file `local.properties` del progetto come:
   `NEWS_API_KEY=inserisci_la_tua_chiave_qui`
   Il progetto è configurato per leggere questa proprietà tramite BuildConfig.
4. Avvia l'app.

## Scelte Architetturali

Il progetto segue l'architettura **MVVM (Model-View-ViewModel)** con una netta separazione delle responsabilità tramite i principi della **Clean Architecture**:

- **Presentation Layer:** Sviluppato interamente in **Jetpack Compose**. Il `NewsViewModel` espone uno `StateFlow` derivato tramite combinazione di più sorgenti (query + stato rete),
  seguendo un approccio reattivo. La comunicazione degli eventi "one-shot" (come lo scroll della lista o la visualizzazione di Snackbar) avviene tramite `Channel` per evitare effetti collaterali indesiderati durante la navigazione o la ricomposizione.
- **Domain Layer:** Contiene la logica di business pura. Sono stati implementati **Use Cases** specifici (`SearchNewsUseCase`, `GetNewsUseCase`, `ToggleFavoriteUseCase`) per rendere la logica riutilizzabile e facilmente testabile, isolando il ViewModel dai dettagli implementativi del repository.
- **Data Layer (Offline-First):** Il `NewsRepositoryImpl` implementa il pattern *Single Source of Truth*. L'applicazione adotta una strategia **Offline-First**: i risultati ottenuti tramite Retrofit vengono immediatamente persistiti nel database locale (Room). La UI osserva i flussi di dati (Flow) provenienti esclusivamente dal database. Questo garantisce che gli articoli precedentemente recuperati tramite una ricerca siano consultabili in qualsiasi momento, anche in assenza di connessione.

## Librerie Utilizzate

- **Jetpack Compose:** Per la UI dichiarativa.
- **Retrofit & OkHttp:** Per il networking e la comunicazione con NewsAPI.
- **Room:** Per la persistenza dei dati e la gestione dei preferiti.
- **Hilt:** Per la gestione del grafo delle dipendenze.
- **Coil:** Per la gestione asincrona delle immagini con sistemi di fallback.
- **Kotlin Coroutines & Flow:** Per la gestione della programmazione reattiva e asincrona.

## Test Unitari e Qualità

È stata realizzata una suite di test unitari utilizzando **MockK** per il mocking e **Turbine** per il testing dei Flow, coprendo i diversi layer dell'architettura:

- **ViewModel Test:** Verifica che la UI reagisca correttamente ai cambiamenti di stato (Success, Loading) e che gli eventi di errore (Snackbar) vengano emessi correttamente al verificarsi di eccezioni o assenza di risultati.
- **Repository Test:** Valida il coordinamento tra sorgente dati remota e locale, assicurando che i dati scaricati dall'API vengano correttamente inseriti nel database Room.
- **UseCase Test:** Verifica che la logica di business sia isolata e che gli errori vengano correttamente mappati in `Result` per il consumo da parte del ViewModel.

Esempio di esecuzione:
```bash
./gradlew test
```

## Evoluzioni Future

Se avessi avuto più tempo avrei implementato/perfezionato i seguenti punti:
1. **Condivisione e Apertura Browser:** permettere all'utente di condividere l'articolo con altre app o di aprirlo nel browser di sistema per leggere il contenuto originale completo.
2. **Schermata dedicata ai Preferiti:** Implementazione di una sezione separata per visualizzare esclusivamente gli articoli salvati dall'utente, magari tramite bottom bar.
3. **Logica di svuotamento cache:** Implementazione di una funzione per eliminare automaticamente gli articoli più vecchi o non contrassegnati come preferiti per ottimizzare lo storage.
4. **Paginazione (Paging 3):** Integrazione della libreria Paging per gestire lo scroll infinito e il caricamento incrementale dei dati.
5. **Miglioramento conversioni errori** Mapper appositi tra i vari layer data->domain->presentation
6. **Miglioramento UI:** Implementazione Tema Icona Animazioni SplashScreen

---

### Organizzazione dei Package
```text
com.example.newsapp
├── core/                # Networking, Database, DI modules, Navigation
├── feature/news/
│   ├── data/            # Repository impl, DataSources (Local/Remote)
│   ├── domain/          # Model, Repository Interface, Use Cases
│   └── presentation/    # ViewModels, UI Screens, Compose Components
```