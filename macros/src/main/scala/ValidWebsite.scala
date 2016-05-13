import java.net.URL

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object ValidWebsite {
  def URL(url: String): URL = macro validWebsiteImpl

  def validWebsiteImpl(c: Context)(url: c.Expr[String]) = {
    import c.universe._
    val q"${urlValue: String}" = url.tree
    try {
      val parsed = new URL(urlValue)
      if (parsed.getProtocol != "http" && parsed.getProtocol != "https") {
        c.abort(c.enclosingPosition, s"$urlValue isn't a website! It must be http(s)!")
      } else {
        c.info(c.enclosingPosition, s"$urlValue is a nice website", force = false)
      }
    } catch {
      case t: Throwable =>
        c.abort(c.enclosingPosition, s"Exception during parsing $t")
    }
    q"new java.net.URL($urlValue)"
  }

  implicit class Website(stringContext: StringContext){
    @inline def website(args: Any*): URL = macro validWebsiteImpl2
  }

  def validWebsiteImpl2(c: Context)(args: c.Expr[Any]*) = {
    import c.universe._

    // parts from StringContext.parts
    val parts = c.prefix.tree match {
      case Apply(_, List(Apply(_, partTrees))) =>
        partTrees map { case q"${const: String}" => const }
      case _ =>  c.abort(c.enclosingPosition, "invalid")
    }

    val urlValue = parts match {
      case List(url) if args.isEmpty => url
      case _ => c.abort(c.enclosingPosition,"Only a single string value is supported")
    }

    try {
      val parsed = new URL(urlValue)
      if (parsed.getProtocol != "http" && parsed.getProtocol != "https") {
        c.abort(c.enclosingPosition, s"$urlValue isn't a website! It must be http(s)!")
      } else {
        c.info(c.enclosingPosition, s"$urlValue is a nice website", force = false)
      }
    } catch {
      case t: Throwable =>
        c.abort(c.enclosingPosition, s"Exception during parsing $t")
    }
    q"new java.net.URL($urlValue)"
  }

}
