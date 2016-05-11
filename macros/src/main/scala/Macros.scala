import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
import java.util.Properties
import java.io.FileInputStream

object Macro{


def divC(a:Int,d:Int):Int= macro divByConstImpl
def divByConstImpl(c: Context)(a: c.Expr[Int],d:c.Expr[Int])={
	import c.universe._
	val Literal(Constant(dVal: Int)) = d.tree
	if(dVal<=0){
		c.abort(c.enclosingPosition, "Only positive values supported")
	}
	
	val allPowersOf2=Stream.iterate(1)(_<<1).takeWhile(_<=dVal)
	val powersOf2 =allPowersOf2.filter((x:Int)=> (x & dVal) != 0).map(_-1).toList

	val result= powersOf2.map{
		case 0 => q"$a"
		case x => q"$a >> $x"
	}.reduceLeft((left,right)=>q"$left+$right")
	
	//println(result)
	
	result
	/*q"""
	$a/$dVal
	"""
	*/
}
}