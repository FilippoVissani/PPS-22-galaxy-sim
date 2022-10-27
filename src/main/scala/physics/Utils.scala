package physics
import physics.Pair

import scala.annotation.targetName

type Position = Pair
type Speed = Double
type SpeedVector = Pair
type GravityForceVector = Pair
type Mass = Double
type GForce = Double


case class Pair(x: Double, y: Double)

object Pair:
  def sum(p1: Pair, p2: Pair): Pair =
    Pair(p1.x + p2.x, p1.y + p2.y)
  def distance(p1: Pair, p2: Pair): Double =
    Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2))
  def module(p: Pair): Double =
    Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2))

  extension (p: Pair)
    @targetName("sumOperator")
    def +(other: Pair) = sum(p, other)
    @targetName("distanceOperator")
    def <->(other: Pair) = distance(p, other)
    @targetName("moduleOperator")
    def || = module(p)
    @targetName("unaryMinusOperator")
    def unary_- = Pair(-p.x, -p.y)