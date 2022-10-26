# Implementazione

## Programmazione funzionale
### For comprehension
### Higher-order functions
 Una High order function è una funzione che prende in input funzioni come parametri e/o restituisce funzioni come risultato. L'utilizzo di questo meccanismo nella programmazione funzionale permette di applicare in maniera semplice il _pattern Strategy_, oltre a rendere il codice riusabile.

 Sono state utilizzate in diverse parti del progetto, un esempio di high order function è il seguente:
 ```scala
def updateTemperature(f: Temperature => Temperature): CelestialBody = 
    celestialBody.copy(temperature = f(celestialBody.temperature))
 ```
 In questa funzione viene applicata alla temperatura del celestial body la funzione _f_ presa come parametro in input.
### Pattern matching
### Option
### Type members

## Programmazione logica
Il paradigma di programmazione logico è stato utilizzato in questo progetto per identificare i diversi tipi di entità, in base alla massa e alla temperatura.

I termini prolog utilizzati per identificare le entità sono contenuti nel file `EntityIdentifier.pl`. Secondo la logica riportata di seguito le entità vengono classificate in _Interstellar Cloud, Asteroid, Planet_ e _Star_.
```prolog
typeOfEntity(Temp, Mass, X) :- 1000 < Temp, typeOfStar(Mass, X), !.
typeOfEntity(Temp, _, interstellarCloud) :- 100 < Temp, !.
typeOfEntity(Temp, _, planet) :- 0 < Temp, !.
typeOfEntity(Temp, _, asteroid).
```
A loro volta le Star vengono identificate in _BlackHole, Supernova, Red Super Giant_ e _Massive Star_ in base alla loro massa.
```prolog
minMass(10**40, blackHole).
minMass(10**20, supernova).
minMass(10**10, redSuperGiant).
minMass(0, massiveStar).
typeOfStar(Mass, Star) :- minMass(MinMass, Star), Mass > MinMass.
```

La classe `Scala2P` si occupa di creare l'engine prolog e fornire una funzione per risolvere un goal.
Per controllare il tipo di entità viene utilizzata la classe `EntityIdentifierProlog`.
```scala
def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType = 
    val goal = s"typeOfEntity($temperature, $mass, E)"
    solveOneAndGetTerm(engine, goal, "E") 
```

## Sezioni personali
### Barzi Eddie
Inizialmente mi sono occupato di gestire il ciclo di vita delle entità, sfruttando Prolog per l'identificazione delle stesse. Le classi relative a questo lavoro sono le seguenti:
- `Scala2P`
- `EntityIdentifierProlog`
- `Lifecycle`
- `OperationsOnCelestialBody`

Successivamente sono passato allo sviluppo delle classi per calcolare e visualizzare le statistiche della simulazione:
- `Statistics`
- `PieChart`

Infine ho provveduto ad integrare le statistiche della simulazione nella schermata principale: 
- `SimulationGUI`

### Cortecchia Angela
### Micelli Leonardo
Il mio ruolo all'interno del team di sviluppo è consistito nello sviluppare, assieme a Cortecchia, una libreria per calcoli fisici da utilizzare all'interno della simulazione. In particolare, il mio compito è stato quello di modellare e sviluppare la logica riguardante la rilevazione di collisioni tra entità della simulazione e la risoluzione di eventuali impatti tra esse. I sorgenti riguardanti il mio lavoro si trovano dunque nel package `physics.collisions`.
La struttura interna di tale package si ispira alla suddivisione in package della libreria scala `cats`: all'interno troviamo un package per ogni type class sviluppata, un package `instances` contenente le implementazioni di libreria per ogni type class, un package `syntax` contenente estensioni che possono arricchire l'uso delle type class presenti nella libreria, ed infine un package `monads` contenente le monadi sviluppate. Questa suddivisione permette ad un utente della libreria di utilizzare solamente i concetti e le funzionalità che gli sono strettamente necessarie, partendo quindi da un core molto semplice (quello della sola type class) e aggiungendo tramite importazione ulteriori funzionalità o sintassi ad hoc. All'interno del package collisions troviamo dunque:
- Type classes:
  - `Intersection`: Questa type class incapsula la logica di intersezione tra due elementi dello stesso tipo;
  - `Impact`: agisce come una sorta di monoide (o combinatore), tuttavia in questa sede si è preferito distinguere i due concetti per attenersi meglio al dominio delle collisioni;

- Instances:
  - `IntersectionInstances`
  - `ImpactInstances`

- Librerie di utility implementate attraverso pattern Singleton:
  - `CollisionEngine`: racchiude in un unica API le funzionalità legate ad intersezione ed impatto, facendo uso del pattern strategy;

- Monads:
  - `Collider`: permette di utilizzare le API di collisione all'interno di for-comprehension;

- Syntax:
  - `CollisionSyntax`: arricchisce un generico tipo con operatori che sfruttano le API di collisione; 
### Vissani FIlippo