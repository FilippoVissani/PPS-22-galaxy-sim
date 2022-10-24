# Design di dettaglio
## Pattern utilizzati
### Strategy
Il pattern Strategy consiste nell'isolare un algoritmo o una strategia al di fuori di una classe, per fare in modo che quest'ultima possa variare dinamicamente il suo comportamento. In Scala è applicato in maniera semplice ed efficace con l'uso delle high order functions, un esempio è il seguente: 
```scala
def updateMass(f: Mass => Mass): CelestialBody = 
    celestialBody.copy(mass = f(celestialBody.mass))
```
`updateMass` è una funzione alla quale viene iniettata la strategia da utilizzare per modificare la massa.

### Factory
### Singleton
### Facade
Il Facade pattern consiste nel fornire un'interfaccia semplice per nascondere alcuni blocchi di codice più complessi.
In questo progetto il pattern Facade è stato utilizzato per fornire la seguente interfaccia, la quale semplifica l'utilizzo del grafico a torta della libreria _JFreeChart_:
```scala
trait PieChart:
  def wrapToPanel: ChartPanel
  def title: String
  def setValue(key: String, value: Double): Unit
  def clearAllValues(): Unit
```

### Pimp my library
Pimp my library è un pattern funzionale che soddisfa la necessità di aggiungere un metodo a una classe già esistente, senza modificarla. In Scala si può implementare utilizzando un _extension method_.
```scala
object OperationsOnCelestialBody:
    extension (celestialBody: CelestialBody)
        def updateMass(f: Mass => Mass): CelestialBody = 
            celestialBody.copy(mass = f(celestialBody.mass))
        def updateTemperature(f: Temperature => Temperature): CelestialBody = 
            celestialBody.copy(temperature = f(celestialBody.temperature))
```
In questo caso è stato creato un oggetto `OperationsOnCelestialBody` il quale, se importato nella classe desiderata, permette di utilizzare i metodi `updateMass` e `updateTemperature` come se fossero dei metodi definiti nella classe `CelestialBody`.

### Adapter
### Type classes

## Struttura del codice