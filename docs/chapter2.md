# Requisiti

## Requisiti di business
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>1.1</td><td>La simulazione deve permettere di visualizzare l'evoluzione di una galassia, dove sono presenti le seguenti entità: stelle, pianeti, asteroidi e nuvole di gas.</td></tr>
<tr><td>1.2</td><td>Gli elementi presenti all'interno della simulazione hanno un ciclo di vita, possono collidere e modificarsi.</td></tr>
<tr><td>1.3</td><td>Le stelle possono essere di tipi differenti, con conseguenze sul loro comportamento.</td></tr>
</table>

## Requisiti utente
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>2.1</td><td>Rappresentazione 2D della simulazione della galassia.</td></tr>
<tr><td>2.1.1</td><td>Rappresentazione delle entità presenti all'interno della galassia.</td></tr>
<tr><td>2.1.2</td><td>Rappresentazione dell'orbita degli elementi della simulazione, se presente.</td></tr>
<tr><td>2.1.3</td><td>Rappresentazione del tempo della simulazione.</td></tr>
<tr><td>//TODO</td><td>Log dettagliato della simulazione in tempo reale. //todo CHECK</td></tr> 
</table>

## Requisiti funzionali
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>3.1</td><td>L'andamento della simulazione deve essere scandito da un tempo virtuale.</td></tr>
<tr><td>3.2</td><td>Inizialmente si presuppone la presenza di nuvole di gas nella simulazione, le quali si muovono liberamente nello spazio, orbitando attorno ad un buco nero posto al centro della galassia.</td></tr>
<tr><td>3.2.1</td><td>Nel momento in cui le nuvole di gas si concentrano in dei punti di sufficiente densità causano un'esplosione che genera una stella.</td></tr>
<tr><td>//TODO </td><td>Nel momento in cui si genera una stella, viene anche creato un nuovo sistema di entità che le orbitano attorno.</td></tr>
<tr><td>3.4</td><td>Le entità definite nel punto 1.1 si muovono seguendo un'orbita ellittica.</td></tr>
<tr><td>3.5</td><td>
Per ogni entità devono essere definite tipologia, massa, posizione, temperatura, velocità e attrazione gravitazionale.
</td></tr>
<tr><td>3.6</td><td>
Le stelle hanno un ciclo di vita, durante il quale la sua massa cambia.
</td></tr>
<tr><td>3.7</td><td>
La tipologia dell'entità cambia al variare di massa e temperatura della stesso.
</td></tr>
<tr><td>3.8</td><td>
Il risultato della collisione di due entità dipende dalle caratteristiche delle entità.
</td></tr>
<tr><td>3.8.1</td><td>
Se due entità collidono, c'è un aumento di massa di una e la disgregazione dell'altra.
</td></tr>
<tr><td>3.9</td><td>
L'universo della simulazione è toroidale.
</td></tr>
</table>

## Requisiti non funzionali
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>4.1</td><td>
Il sistema deve essere eseguibile sui principali sistemi operativi desktop, ovvero Windows, Linux e MacOS
</td></tr>
<tr><td>4.2</td><td>
L'interfaccia grafica deve essere semplice e intuitiva, per fare in modo che anche un utente non esperto del dominio sia in grado di comprendere il funzionamento del sistema.</td></tr>
</table>


## Requisiti di implementazione
<table>
<tr><th>ID/Numero</th><th>Testo del requisito</th></tr>
<tr><td>5.1</td><td>I linguaggi di programmazione utilizzati devono essere Scala 3.2.0 e Prolog</td></tr>
<tr><td>5.2</td><td>Utilizzo di Scalatest</td></tr>
<tr><td>5.3</td><td>Utilizzo di sbt 1.7.1 come build tool</td></tr>
<tr><td>5.4</td><td>Plugin sbt-scoverage per la code coverage</td></tr>
</table>
