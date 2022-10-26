# Requisiti

## Requisiti di business
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>1.1</td><td>La simulazione deve permettere di visualizzare l'evoluzione di una galassia, dove sono presenti le seguenti entità: stelle, pianeti, asteroidi e nuvole di gas.</td></tr>
<tr><td>1.2</td><td>Gli elementi presenti all'interno della simulazione hanno un tempo di vita, possono collidere e modificarsi.</td></tr>
<tr><td>1.3</td><td>Le stelle possono essere di tipi differenti, con conseguenze sul loro comportamento.</td></tr>
</table>

## Requisiti utente
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>2.1</td><td>Rappresentazione 2D della simulazione della galassia.</td></tr>
<tr><td>2.1.1</td><td>Rappresentazione delle entità presenti all'interno della galassia.</td></tr>
<tr><td>2.1.2</td><td>Rappresentazione dell'orbita degli elementi della simulazione, se presente.</td></tr>
<tr><td>2.1.3</td><td>Rappresentazione del tempo della simulazione.</td></tr>
<tr><td>2.2</td><td>Log dettagliato della simulazione in tempo reale.</td></tr>
</table>

## Requisiti funzionali
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>3.1</td><td>L'andamento della simulazione deve essere scandito da un tempo virtuale.</td></tr>
<tr><td>3.2</td><td>Inizialmente si presuppone la presenza di nuvole di gas nella simulazione, le quali si muovono liberamente nello spazio.</td></tr>
<tr><td>3.2.1</td><td>Nel momento in cui le nuvole di gas si concentrano in dei punti a elevata densità causano un'esplosione che genera una stella.</td></tr>
<tr><td>3.2.2</td><td>Nel momento in cui si genera una stella, viene anche creato un nuovo sistema di entità che le orbitano attorno.</td></tr>
<tr><td>??</td><td>Le entità che possono essere generate da quelle definite nel punto 3.3 sono: stelle, pianeti, asteroidi.
</td></tr>
<tr><td>3.4</td><td>Le entità definite nel punto 1.1 si muovono seguendo un'orbita ellittica</td></tr>
<tr><td>3.5</td><td>
Per ogni entità devono essere definite tipologia, massa, posizione, velocità e attrazione gravitazionale.
</td></tr>
<tr><td>3.6</td><td>
Le stelle hanno un ciclo di vita, durante il quale la sua massa cambia.
</td></tr>
<tr><td>??</td><td>
Da una nuvola di gas si può creare una massive star o una low-mass star.
</td></tr>
<tr><td>3.9</td><td>
Una massive star diventa una red supergiant.
</td></tr>
<tr><td>3.10</td><td>
Una red supergiant diventa una supernova.
</td></tr>
<tr><td>3.11</td><td>
Una supernova diventa un buco nero.
</td></tr>
<tr><td>3.12</td><td>
Il risultato della collisione di due entità dipende dalle caratteristiche delle entità.
</td></tr>
<tr><td>3.12.1</td><td>
Un buco nero, nel momento in cui ingloba un'entità, diventa più grande.
</td></tr>
<tr><td>3.12.2</td><td>
Se due pianeti collidono si forma un pianeta più grande.
</td></tr>
<tr><td>3.12.3</td><td>
Se due stelle collidono, la stella di massa maggiore ingloba l'altra.
</td></tr>
<tr><td>3.12.4</td><td>
Se un pianeta e una stella collidono, la stella ingloba il pianeta.
</td></tr>
<tr><td>3.12.5</td><td>
Se un nuvola di gas collide con un'altra entità ne aumenta la massa.
</td></tr>
</table>

## Requisiti non funzionali
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>4.1</td><td>//TODO Scrivere delle performance</td></tr>
<tr><td>4.2</td><td>Il sistema deve essere eseguibile sui principali sistemi operativi desktop, ovvero Windows, Linux e MacOS</td></tr>
<tr><td>4.3</td><td>L'interfaccia grafica deve essere semplice e intuitiva, per fare in modo che anche un utente non esperto del dominio sia in grado di comprendere il funzionamento del sistema.</td></tr>
</table>


## Requisiti di implementazione
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>5.1</td><td>I linguaggi di programmazione utilizzati devono essere Scala 3.2.0 e Prolog</td></tr>
<tr><td>5.2</td><td>Utilizzo di Scalatest</td></tr>
<tr><td>5.3</td><td>Utilizzo di sbt 1.7.1 come build tool</td></tr>
<tr><td>5.4</td><td>Plugin sbt-scoverage per la code coverage</td></tr>
</table>