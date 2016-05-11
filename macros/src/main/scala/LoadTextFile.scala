import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object LoadTextFile {
  def loadFile(file: String): String = macro loadFileImpl

  def loadFileImpl(c: Context)(file: c.Expr[String]) = {
    import c.universe._
    val Literal(Constant(fileName: String)) = file.tree
    val source = scala.io.Source.fromFile(fileName)
    val contents = source.mkString
    source.close()

    Literal(Constant(contents))
  }
}
