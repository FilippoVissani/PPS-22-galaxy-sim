package galaxy_sim.model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class EntityTest extends AnyFunSuite:
    test("Entity correctly defines constructors"){
        "Entity(name = \"Star1\", mass = 100, volume = 100, speed = Pair(100, 100), position = Pair(100, 100))" should compile
    }
