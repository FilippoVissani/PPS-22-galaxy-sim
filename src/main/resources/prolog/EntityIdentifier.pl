/*
Mass and Temperature defines the entity type.

the temperature defines if the entity is a Star, Planet, Asteroid or InterstellarCloud:
	1000 < Temp <= _ 		is a Star.
	100 < Temp <= 1000 		is a Planet
	0 	< Temp <= 100 		is an Asteroid
		_ < Temp <= 0 		is an InterstellarCloud

the mass defines the type of the Star.
*/

massiveStar.
redSuperGiant.
supernova.
blackHole.
planet.
asteroid.
interstellarCloud.

typeOfStar(Mass, blackHole) :- Mass > 1000, !.
typeOfStar(Mass, supernova) :- Mass > 800, !.
typeOfStar(Mass, redSuperGiant) :- Mass > 300, !.
typeOfStar(Mass, massiveStar) :- Mass > 0, !.

typeOfEntity(Temp, Mass, X) :- 1000 < Temp, typeOfStar(Mass, X), !.
typeOfEntity(Temp, _, planet) :- 100 < Temp, !.
typeOfEntity(Temp, _, asteroid) :- 0 < Temp, !.
typeOfEntity(Temp, _, interstellarCloud).