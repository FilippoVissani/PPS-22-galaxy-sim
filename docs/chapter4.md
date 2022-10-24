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
### Adapter
### Type classes

## Struttura del codice