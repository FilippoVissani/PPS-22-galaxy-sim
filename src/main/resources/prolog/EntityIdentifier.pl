/*
Mass and Temperature defines the entity type.

the temperature defines if the entity is a Star, Planet, Asteroid or InterstellarCloud:
	1000 < Temp <= _ 		is a Star.
	100 < Temp <= 1000 		is a InterstellarCloud
	0 	< Temp <= 100 		is an Planet
		_ < Temp <= 0 		is an Asteroid

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

minMassBlackHole(10**40, blackHole).
minMassSupernova(10**20, supernova).
minMassRedSuperGiant(10**10, redSuperGiant).
minMassMassiveStar(0, massiveStar).

typeOfStar(Mass, blackHole) :- minMassBlackHole(X, blackHole), Mass > X, !.
typeOfStar(Mass, supernova) :- minMassSupernova(X, supernova), Mass > X, !.
typeOfStar(Mass, redSuperGiant) :- minMassRedSuperGiant(X, redSuperGiant), Mass > X, !.
typeOfStar(Mass, massiveStar) :- minMassMassiveStar(X, massiveStar), Mass > X, !.

typeOfEntity(Temp, Mass, X) :- 1000 < Temp, typeOfStar(Mass, X), !.
typeOfEntity(Temp, _, interstellarCloud) :- 100 < Temp, !.
typeOfEntity(Temp, _, planet) :- 0 < Temp, !.
typeOfEntity(Temp, _, asteroid).