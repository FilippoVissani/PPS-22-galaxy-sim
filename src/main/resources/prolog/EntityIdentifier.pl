/*
Mass and Temperature defines the entity type.

the temperature defines if the entity is a Star, Planet, Asteroid or InterstellarCloud:
	1000 < Temp <= _ 		is a Star.
	100 < Temp <= 1000 		is a InterstellarCloud
	50 	< Temp <= 100 		is an Planet
		_ < Temp <= 50		is an Asteroid

the mass defines the type of the Star.
*/

/*
entity types:

massiveStar.
redSuperGiant.
supernova.
blackHole.
planet.
asteroid.
interstellarCloud.
*/

minMass(10**40, blackHole).
minMass(10**20, supernova).
minMass(10**10, redSuperGiant).
minMass(0, massiveStar).

typeOfStar(Mass, Star) :- minMass(MinMass, Star), Mass > MinMass.

typeOfEntity(Temp, Mass, X) :- 1000 < Temp, typeOfStar(Mass, X), !.
typeOfEntity(Temp, _, interstellarCloud) :- 100 < Temp, !.
typeOfEntity(Temp, _, planet) :- 50 < Temp, !.
typeOfEntity(Temp, _, asteroid).