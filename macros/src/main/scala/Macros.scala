import scala.reflect.macros.Context
import scala.language.experimental.macros

object Macro{
def loadFile(file:String):String = macro loadFileImpl
def loadFileImpl(c:Context)(file:c.Expr[String])={
  import c.universe._
  val Literal(Constant(file_name: String)) = file.tree
  val source = scala.io.Source.fromFile(file_name)
  val contents = source.mkString
  source.close()

  Literal(Constant(contents))
}
}