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
### Vissani FIlippo