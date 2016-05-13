import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object MultiplyByConst{

	case class PowerOf2(power: Int, value: Int) {
		def next = PowerOf2(power + 1, value << 1)
	}

	object PowerOf2 {
		val zero = PowerOf2(0, 1)
	}

	def multC(a: Int, d: Int): Int = macro multByConstImpl

	def multByConstImpl(c: Context)(a: c.Expr[Int], d: c.Expr[Int]) = {
		import c.universe._
		val Literal(Constant(dVal: Int)) = d.tree
		if (dVal <= 0) {
			c.abort(c.enclosingPosition, "Only positive values supported")
		}

		def allPowersOf2 = Iterator.iterate(PowerOf2.zero)(_.next).takeWhile(_.value <= dVal)
		def powersOf2 = allPowersOf2.filter(powerOf2 => (powerOf2.value & dVal) != 0).map(_.power)

		val result = powersOf2.map {
			case 0 => q"$a"
			case power => q"$a << $power"
		}.reduceLeft((left, right) => q"$left+$right")

		c.info(c.enclosingPosition,s"resulting AST: $result",force=false)

		result
	}
}
