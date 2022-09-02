# Requisiti

## Requisiti di business
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>1.1</td><td>La simulazione deve permettere di visualizzare l'evoluzione di una galassia, dove sono presenti le seguenti entità: nuvole di gas, stelle, buchi neri e pianeti.</td></tr>
<tr><td>1.2</td><td>Gli elementi presenti all'interno della simulazione hanno un tempo di vita, possono collidere e modificarsi.</td></tr>
<tr><td>1.3</td><td>Stelle e pianeti possono essere di tipi differenti, con conseguenze sul loro comportamento.</td></tr>
</table>

## Requisiti utente
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>2.1</td><td>Rappresentazione 2D della simulazione della galassia.</td></tr>
<tr><td>2.1.1</td><td>Rappresentazione delle entità presenti all'interno della galassia.</td></tr>
<tr><td>2.1.2</td><td>Rappresentazione dell'orbita degli elementi della simulazione, se presente.</td></tr>
<tr><td>2.1.3</td><td>Rappresentazione del tempo della simulazione.</td></tr>
<tr><td>2.2</td><td>Passaggio di parametri tramite file di configurazione.</td></tr>
<tr><td>2.2</td><td>Log dettagliato della simulazione in tempo reale e su file.</td></tr>
</table>

## Requisiti funzionali
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>3.1</td><td>L'andamento della simulazione deve essere scandito da un tempo virtuale.</td></tr>
<tr><td>3.2</td><td>Inizialmente si presuppone la presenza di nuvole di vari tipi di gas nella simulazione, le quali si muovono liberamente nello spazio.</td></tr>
<tr><td>3.2</td><td>Nel momento in cui le nuvole di gas si concentrano in dei punti ad elevata densità causano un'esplosione che genera una stella.</td></tr>
<tr><td>3.3</td><td>In base al punto 3.2, la simulazione deve supportare diverse tipologie di elementi: idrogeno, elio, polvere e metalli.
</td></tr>
<tr><td>3.2</td><td>La creazione di una nuova stella comporta il rilascio di gas residuo, che viene liberato nello spazio.</td></tr>
<tr><td>3.2</td><td>Nel momento in cui si genera una stella, viene anche creato un nuovo sistema di entità che le orbitano attorno.</td></tr>
<tr><td>3.4</td><td>Le entità che possono essere generate da quelle definite nel punto 3.3 sono: stelle, pianeti, asteroidi.
</td></tr>
<tr><td>3.4</td><td>Le entità definite nel punto 3.4 si muovono seguendo un'orbita ellittica</td></tr>
<tr><td>3.4.1</td><td>
Per ogni entità devono essere definite tipologia, massa, posizione, velocità e attrazione gravitazionale.
</td></tr>
<tr><td>3.4.1.2</td><td>
Ogni stella ha un tempo di vita, durante il quale brucia idrogeno o elio, di conseguenza la massa cambia.
</td></tr>
<tr><td>3.4.1.3</td><td>
Le stelle hanno un ciclo di vita.
</td></tr>
<tr><td>3.4.1.3</td><td>
Da una nuvola di gas si può creare una massive star o una low-mass star.
</td></tr>
<tr><td>3.4.1.3</td><td>
Una massive star diventa una red supergiant.
</td></tr>
<tr><td>3.4.1.3</td><td>
Una red supergiant diventa una supernova.
</td></tr>
<tr><td>3.4.1.3</td><td>
Una supernova diventa un buco nero.
</td></tr>
<tr><td>3.4.1.3</td><td>
Una low-mass star diventa una red giant.
</td></tr>
<tr><td>3.4.1.3</td><td>
Una red giant diventa una planetary nebula.
</td></tr>
<tr><td>3.4.1.3</td><td>
Una planetary nebula diventa una white dwarf.
</td></tr>
<tr><td>3.4.1.3</td><td>
Una white dwarf diventa una black dwarf.
</td></tr>
<tr><td>3.4.1.2</td><td>
Il buco nero presente al centro della galassia assorbe ciò che gli orbita attorno fino all'esaurimento delle entità della galassia.
</td></tr>
<tr><td>3.4.1.2</td><td>
Il risultato della collisione di due entità dipende dalle caratteristiche delle entità.
</td></tr>
<tr><td>3.4.1.2</td><td>
Un buco nero, nel momento in cui ingloba un'entità, diventa più grande.
</td></tr>
<tr><td>3.4.1.2</td><td>
Se due pianeti collidono si forma un pianeta più grande.
</td></tr>
<tr><td>3.4.1.2</td><td>
Se due stelle collidono, la stella di massa maggiore ingloba l'altra.
</td></tr>
<tr><td>3.4.1.2</td><td>
Se un pianeta e una stella collidono, la stella ingloba il pianeta.
</td></tr>
<tr><td>3.4.1.2</td><td>
Se un nuvola di gas collide con un'altra entità ne aumenta la massa.
</td></tr>
</table>

## Requisiti non funzionali
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>4.1</td><td>//TODO Scrivere delle performance</td></tr>
<tr><td>4.2</td><td>Il sistema deve essere eseguibile sui principali sistemi operativi desktop, ovvero Windows, Linux e MacOS</td></tr>
<tr><td>4.3</td><td>//TODO usabilità interfaccia utente</td></tr>
</table>


## Requisiti di implementazione
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>5.1</td><td>I linguaggi di programmazione utilizzati devono essere Scala 3.2.0 e Prolog</td></tr>
<tr><td>5.2</td><td>Utilizzo di Scalatest</td></tr>
<tr><td>5.3</td><td>Utilizzo di sbt 1.7.1 come build tool</td></tr>
<tr><td>5.4</td><td>Plugin sbt-scoverage per la code coverage</td></tr>
<tr><td>5.5</td><td>//TODO</td></tr>
</table>
