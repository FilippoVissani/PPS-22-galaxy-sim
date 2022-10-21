# Implementazione

## Programmazione funzionale
### For comprehension
### Higher-order functions
### Pattern matching
### Option
### Type members

## Programmazione logica
Il paradigma di programmazione logico è stato utilizzato in questo progetto per identificare i diversi tipi di entità, in base alla massa e alla temperatura.

I termini prolog utilizzati per identificare le entità sono contenuti nel file `EntityIdentifier.pl`.

La classe `Scala2P` si occupa di creare l'engine prolog e fornire una funzione per risolvere un goal.

Per controllare il tipo di entità viene utilizzata la classe `EntityIdentifierProlog`.

```scala
def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType = 
    val goal = s"typeOfEntity($temperature, $mass, E)"
    solveOneAndGetTerm(engine, goal, "E") 
```