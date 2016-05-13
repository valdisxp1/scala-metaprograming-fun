import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
import java.util.Properties
import java.io.FileInputStream
import scala.collection.JavaConverters._

object LoadProperties {
  def loadProperties(file: String): Map[String, String] = macro loadPropertiesImpl

  def loadPropertiesImpl(c: Context)(file: c.Expr[String]) = {
    import c.universe._

    // 1. (unlift) assume the expression is a string constant and just extract the value
    val q"${fileName: String}" = file.tree

    // 2. do the actual work
    val fis = new FileInputStream(fileName)
    val properties = new Properties
    properties.load(fis)
    fis.close()
    val propertiesMap = properties.asScala.toMap

    // 3. (lift) quasiquotes generates the code for creating a map with the values of propertiesMap
    q"$propertiesMap"
  }


  def loadPropertiesWithoutQ(file: String): Map[String, String] = macro loadPropertiesWithoutQImpl

  def loadPropertiesWithoutQImpl(c: Context)(file: c.Expr[String]) = {
    import c.universe._
    import scala.collection.mutable

    // 1. assume the expression is a string constant and pattern match it
    val Literal(Constant(fileName: String)) = file.tree

    // 2. do the actual work
    val fis = new FileInputStream(fileName)
    val properties = new Properties
    properties.load(fis)
    fis.close()
    val propertiesMap = properties.asScala.toMap

    // 3.1. define the value called builder for the mapBuilder, i.e. val builder = Map.newBuilder[String, String]
    val builderVariable = TermName("builder")
    val typeTag = typeOf[mutable.Builder[(String, String), Map[String, String]]]
    val define = ValDef(Modifiers(), builderVariable, TypeTree(typeTag), reify(Map.newBuilder[String, String]).tree)

    // 3.2. add all the values with += a.k.a. $plus$eq
    def constant(s: String) = c.Expr[String](Literal(Constant(s)))
    val pairs = propertiesMap.map {
      case (key, value) =>
        // create a pair of string constants ("THEkey","valueIShere")
        reify((constant(key).splice, constant(value).splice)).tree
    }
    val adds = pairs.map {
      tree =>
        // create the function call for +=
        Apply(Select(Ident(builderVariable), TermName("$plus$eq")), List(tree))
    }.toList


    // 3.3 collect the resulting immutable Map by calling result on the builder
    val result = Apply(Select(Ident(builderVariable), TermName("result")), List())

    // 3.4. the complete code
    // first do the define followed by all the += calls
    // then collect resulting immutable map
    // this last statement is what will give the block of code its value
    c.Expr[Map[String, String]](Block(
      define :: adds, result
    ))
  }
}
