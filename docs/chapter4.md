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
Il pattern Factory è un design pattern che consiste nel separare le logiche di creazione di un oggetto o dato dalla sua definizione. In scala, questo design pattern è facilmente
implementabile attraverso l'uso del Companion Object: un particolare oggetto singleton che può fungere da contenitore di varie funzioni riguardanti un certo tipo di dato, tra cui
anche funzioni di creazione (un caso notevole di questo è il metodo "apply"). All'interno del progetto possiamo trovare anche funzioni di factory diverse da apply, come nel caso della
libreria "collisions":
```scala
/** Constructs an [[Impact]] starting from a function of the type (A, A) => A. */
def from[A](f: (A, A) => A): Impact[A] = (a1: A, a2: A) => f(a1, a2)
```
Questa funzione di factory, per quanto semplice, permette di realizzare implementazioni della type class Impact in maniera più snella rispetto alla semplice implementazione per estensione,
più propria della object orientation.
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
È un pattern di programmazione funzionale che si basa sul concetto di estensione delle funzionalità di un tipo di dato senza modificarlo. L'approccio tipico della FP è quello di definire
i dati e le funzioni in sorgenti separati, ed eventualmente legarli insieme successivamente attraverso il pimping delle funzioni sui dati. In scala, il meccanismo `extension`
permette di effettuare il pimping di un tipo di dato senza modificarlo. Un esempio di utilizzo di questo pattern all'interno del progetto si può trovare nel file `CollisionSyntax`:
```scala
 extension [A](a: A)
    @targetName("collidesWith")
    def |#|(other: A)(using Intersection[A]): Boolean =
      collides(a, other)

    @targetName("impactWith")
    def |*|(other: A)(using Intersection[A])(using Impact[A]): A =
      impact(a, other)
```
Con l'uso del meccanismo delle estensioni, si aggiungono ad un tipo generico A dei metodi che sfruttano le API di collisione.
Un altro esempio di uso del pattern è il seguente:
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
Il pattern Adapter è facilmente implementabile in Scala attraverso il meccanismo delle _given conversion_. In questo progetto sono state utilizzate nella classe `Scala2P` per migliorare l'integrazione con Prolog, come riportato di seguito:
```scala
given Conversion[String, Term] = Term.createTerm(_)
given Conversion[String, Theory] = Theory.parseLazilyWithStandardOperators(_)
given Conversion[Term, CelestialBodyType] = _.toString match
  case "massiveStar" => MassiveStar
  case "redSuperGiant" => RedSuperGiant
  case "supernova" => Supernova
  case "blackHole" => BlackHole
  case "planet" => Planet
  case "asteroid" => Asteroid
  case "interstellarCloud" => InterstellarCloud
```

### Type classes
La Type Class è una tipologia di classe astratta e parametrizzata sul tipo, che permette di aggiungere un dato comportamento ad un qualunque tipo di dato senza utilizzare
ereditarietà. In scala, si definiscono type classes in questo modo: Combiner[A], dove il tipo A racchiuso tra quadre rappresenta un qualunque tipo chiuso. All'interno del progetto, possiamo trovare
esempi di type class nel package `physics.collisions`, in cui troviamo le type classes `Intersection`, `Impact` e `Collider`

## Struttura del codice
Il codice è stato strutturato in package come descritto nel diagramma seguente:
![Packages](./images/packages_diagram.svg)