# Processo di Sviluppo Adottato

- Processo di sviluppo ispirato a Scrum

## Sprint
- Sprint settimanali

## Product Backlog
## Workflow

Per quanto riguarda la definizione dei task è stato utilizzato Trello,
in questo modo è stato possibile tener traccia dello stato di avanzamento del progetto.
L'organizzazione del repository Git è la seguente:
- È presente un branch _main_, il quale contiene il codice di produzione del progetto. Ogni release è marcata da uno specifico tag.
- Il branch _develop_ contiene la versione del codice che poi andrà in produzione nel branch _main_. In questo branch vengono introdotte le nuove funzionalità che devono essere testate.
- Per lo sviluppo di ogni funzionalità viene creato un branch apposito, che poi verrà unito al branch _develop_.

// TODO AGGIUNGERE GRAFICO WORKFLOW

## Tool Utilizzati

- Come build tool è stato utilizzato SBT.
- Per la suddivisione dei task è stato utilizzato Trello.
- ScalaTest è stato utilizzato per la scrittura dei test.
- Per la formattazione del codice è stato utilizzato sbt-scalafmt.
- Per la creazione del jar eseguibile è stato utilizzato sbt-assembly.
- Per la code coverage è stato utilizzato sbt-scoverage.
- GitHub è stato utilizzato come servizio di hosting per la repository.
- Le GitHub Actions sono state utilizzate per automatizzare l'esecuzione dei test e della code coverage e per la creazione delle release.
- Codecov è stato utilizzato per analizzare le statistiche della code coverage.

