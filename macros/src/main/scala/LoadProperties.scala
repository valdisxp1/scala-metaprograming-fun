import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
import java.util.Properties
import java.io.FileInputStream

object LoadProperties {
  def loadProperties(file: String): Map[String,String] = macro loadPropertiesImpl
  def loadPropertiesImpl(c: Context)(file: c.Expr[String])={
    import c.universe._
    import scala.collection.JavaConverters._

    val q"${fileName: String}" = file.tree

    val fis = new FileInputStream(fileName)
    val properties = new Properties
    properties.load(fis)
    fis.close()

    val propertiesMap = properties.asScala.toMap
    q"$propertiesMap"
  }
}
