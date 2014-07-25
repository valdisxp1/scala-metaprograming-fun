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
  
  def constant(s:String)=c.Expr[String](Literal(Constant(s)))
  val pairs = map.map{case(k,v)=>reify((constant(k).splice,constant(v).splice)).tree}
  
  val mapVariable= newTermName("map")
  
  val typeTag = typeOf[mutable.Builder[(String,String),Map[String,String]]]
  val define = ValDef(Modifiers(), mapVariable,TypeTree(typeTag) , reify(Map.newBuilder[String,String]).tree)
  
  val result = Apply(Select(Ident(mapVariable), newTermName("result")), List())
  
  def add(arg: Tree): Apply = Apply(Select(Ident(mapVariable), newTermName("$plus$eq")), List(arg))
  
  val adds = pairs.map(t=>add(t)).toList
  
  c.Expr[Map[String,String]](Block(
  define :: adds, result
  ))
}
}