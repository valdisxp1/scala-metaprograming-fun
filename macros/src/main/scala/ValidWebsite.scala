import java.net.URL

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object ValidWebsite {
  def validWebsite(url: String): URL = macro validWebsiteImpl

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

  /*implicit class Website(stringContext: StringContext){
    def website() = macro validWebsiteImpl
  }*/
}
