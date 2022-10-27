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
### Contextual programming
Il concetto di `contesto` di una funzione si riferisce a quegli argomenti che vengono usati per influenzare il modo in cui la funzione produce il suo output a partire dagli input. Questa tipologia di modellazione delle funzioni è facilmente implementabile in Scala attraverso il meccanismo delle Given instance: si possono definire dei parametri di contesto di una funzione con la keyword `using`. Quando la funzione verrà invocata, il compilatore si occuperà di cercare nello scope corrente dei parametri di contesto appropriati definiti con la keyword `given`. Se la ricerca ha successo, la funzione utilizzerà tali parametri senza bisogno che vengano specificati come input dall'utente.
Un esempio di funzione che fa uso di given instances è la seguente:
```scala
def collides[A](a1: A, a2: A)(using Intersection[A]): Boolean =
    a1 intersects a2
```
Di seguito un esempio di definizione di parametro di contesto attraverso given instance:
```scala
given CircleToCircleIntersection: Intersection[CircleCollisionBox] =
    Intersection.from((c1, c2) => {
      val dx = c1.origin.x - c2.origin.x
      val dy = c1.origin.y - c2.origin.y
      val dist = Math.sqrt(dx * dx + dy * dy)
      dist < c1.radius + c2.radius
    })
```

Anche la funzione `entityOneStep` della classe `Lifecycle` fa uso di given instances:
```scala
def entityOneStep[A](entity: CelestialBody, bodyType: A)(using lifecycleRules: LifecycleRules[A]): (CelestialBody, A) =
  lifecycleRules.updateMassAndTemperature(entity, bodyType)
```
Come si può vedere dal codice viene infatti richiesto un parametro di contesto, oltre ai parametri di input _entity_ di tipo CelestialBody e _bodyType_ generico. In questo modo si possono definire i parametri di contesto per ogni _bodyType_ attraverso le given instances.

Nel progetto è presente una sola given instance in quanto il _bodyType_ corrisponde all'enum `CelestialBodyType`. Avendo però prodotto questa implementazione è facile e immediato aggiungere delle given instances nel caso in cui si voglia estendere il sistema, inserendo ad esempio delle case class invece dell'enum per indicare i _bodyType_.
```scala
given LifecycleRules[CelestialBodyType] with
  override def updateMassAndTemperature(celestialBody: CelestialBody, bType: CelestialBodyType): (CelestialBody, CelestialBodyType) = ...
```

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
Per controllare il tipo di entità viene utilizzata la classe `EntityIdentifier`.
```scala
def checkEntityType(mass: Mass, temperature: Temperature): CelestialBodyType = 
    val goal = s"typeOfEntity($temperature, $mass, E)"
    solveOneAndGetTerm(engine, goal, "E") 
```

## Sezioni personali
### Barzi Eddie
Inizialmente mi sono occupato di gestire il ciclo di vita delle entità, sfruttando Prolog per l'identificazione delle stesse. Le classi relative a questo lavoro sono le seguenti:
- `Scala2P`
- `EntityIdentifier`
- `Lifecycle`
- `OperationsOnCelestialBody`

Successivamente sono passato allo sviluppo delle classi per calcolare e visualizzare le statistiche della simulazione:
- `Statistics`
- `PieChart`

Infine ho provveduto ad integrare le statistiche della simulazione nella schermata principale: 
- `StatisticsPanel`
- `SimulationGUI`

La seguente tabella riporta i meccanismi avanzati e/o i pattern utilizzati nelle classi indicate:

<table>
<tr><th>Classe</th><th>Meccanismi o pattern utilizzati</th></tr>
<tr><td>Scala2P</td><td>- Implicit conversions</td></tr>
<tr><td>EntityIdentifier</td><td> - Singleton</td></tr>
<tr><td>Lifecycle</td><td>- Contextual programming </br> - Type classes </br> - Pattern matching </br> - Infix extension method</td></tr>
<tr><td>OperationsOnCelestialBody</td><td>- Extension method </br> - Higher-order functions</td></tr>
<tr><td>Statistics</td><td>- Type alias </br> - Higher-order functions</td></tr>
<tr><td>PieChart</td><td>- Facade </br> - Factory</td></tr>
<tr><td>StatisticsPanel</td><td>- Tail recursive function</td></tr>
<tr><td>SimulationGUI</td><td>/</td></tr>
</table>

### Cortecchia Angela
Il mio scopo principale è stato quello di creare una libreria di calcoli fisici, assieme a Micelli, in dettaglio mi sono occupata della comprensione dei calcoli e delle formule sulle leggi gravitazionali applicabili nello spazio, formulate da Isaac Newton.
Dopo la comprensione di tali formule mi sono occupata della loro implementazione per poterle rendere utilizzabili in una simulazione bi-dimensionale.
I sorgenti riguardanti questo lavoro si trovano nel package `physics.dynamics`.
Tale package è stato suddiviso in maniera tale da permettere agli utenti della libreria di usufruire dei vari calcoli e funzioni in maniera indipendente, oppure permette utilizzare le varie formule complete ed ottenere il risultato finale delle varie funzioni.
Le classi all'interno di questo package sono dunque:
- `PhysicalEntity`: contiene un'entità rappresentabile nell'universo fisico e costanti utili per formule e simulazione;
- `PhysicsFormulas`: type class che contiene logica riguardante le varie formule e calcoli per l'implementazione delle formule gravitazionali;
- `GravitationLaws`: type class che contiene l'implementazione delle formule gravitazionali.

Dopodiché ho provveduto all'implementazione di una funzionalità che permettesse di trovare l'entità di riferimento di un corpo, ovvero l'entità attorno alla quale il corpo orbita. 
Situata nel package `utils`, la type class `EntityReferenceDetector`, dato il corpo celeste soggetto dei calcoli e un set di tutti i corpi presenti nella simulazione, si occupa di calcolare quale sia effettivamente l'entità attorno alla quale il soggetto deve orbitare.
Per capirlo vengono utilizzati calcoli inerenti all'astrodinamica.

Infine mi sono occupata d'implementare nella schermata principale della simulazione un'area in cui si potessero vedere le informazioni dei vari corpi presenti.

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

### Vissani Filippo
In questo progetto mi sono occupato di definire, implementare e testare le componenti principali
del pattern _MVC_ e gli attori che ne fanno uso. Entrando più nel dettaglio, le parti specifiche
che mi riguardano sono le seguenti:

Introduzione degli attori (package `galaxy_sim.actors`) e definizione delle loro interazioni:
- `SimulationManagerActor`
- `CelestialBodyActor`
- `ControllerActor`
- `ViewActor`

Introduzione dei seguenti componenti del _Model_, presenti nel package `galaxy_sim.model`:
- `Boundary`: Definisce i confini di una simulazione
- `CelestialBody`: Rappresenta lo stato di un corpo celeste
- `Simulation`: Rappresenta lo stato della simulazione

Introduzione dei seguenti componenti della _View_, presenti nel package `galaxy_sim.view`:
- `SwingGUI`: interfaccia grafica scritta utilizzando la libreria Swing
- `View`: gestisce l'interfaccia grafica
