import scala.reflect.macros.Context
import scala.language.experimental.macros
import java.util.Properties
import java.io.FileInputStream

object Macro{
def loadFile(file:String):String = macro loadFileImpl
def loadFileImpl(c:Context)(file:c.Expr[String])={
  import c.universe._
  val Literal(Constant(fileName: String)) = file.tree
  val source = scala.io.Source.fromFile(fileName)
  val contents = source.mkString
  source.close()

  Literal(Constant(contents))
}

def loadProperties(file: String): Map[String,String] = macro loadPropertiesImpl
def loadPropertiesImpl(c: Context)(file: c.Expr[String])={
  import c.universe._
  import scala.collection.JavaConverters._
  
  val q"${fileName: String}" = file.tree
  
  val fis = new FileInputStream(fileName)
  val properties = new Properties
  properties.load(fis)
  fis.close
  
  val propertiesMap = properties.asScala.toMap
  q"$propertiesMap"
}

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