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
  import scala.collection.mutable
  val Literal(Constant(fileName: String)) = file.tree
  val fis = new FileInputStream(fileName)
  val properties = new Properties
  properties.load(fis)
  fis.close
  
  val map = properties.asScala
  
  val pairs = map.map{case(k,v)=>reify((k,v))}
  
  def mapVariable= newTermName("map")
  
  val typeTag = typeOf[mutable.Builder[(String,String),Map[String,String]]]
  val define = ValDef(Modifiers(), mapVariable,TypeTree(typeTag) , reify(Map.newBuilder[String,String]).tree)
  
  val result = Apply(Select(Ident(mapVariable), newTermName("result")), List())
  
  val add = Apply(Select(Ident(mapVariable), newTermName("$plus$eq")), List(reify{("x","y")}.tree))
  
  c.Expr[Map[String,String]](Block(
  List(define,
  add), 
  result
  ))
}
}