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

def loadProperties(file:String):Map[String,String] = macro loadPropertiesImpl
def loadPropertiesImpl(c:Context)(file:c.Expr[String])={
  import c.universe._
  import scala.collection.JavaConverters._
  val Literal(Constant(fileName: String)) = file.tree
  val fis = new FileInputStream(fileName)
  val properties = new Properties
  properties.load(fis)
  fis.close
  
  val map = properties.asScala
  
  //c.Expr[Map[String,String]]
  reify(Map.empty[String,String])
}
}