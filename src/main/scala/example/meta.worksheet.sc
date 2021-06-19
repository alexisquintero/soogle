6 + 1  /*>  : Int = 7  */

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
// import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
// import net.ruippeixotog.scalascraper.model._

val browser = JsoupBrowser()  /*>  : net.ruippeixotog.scalascraper.browser.Browser = net.ruippeixotog.scalascraper.browser.Jsou…  */
val doc = browser.get("http://www.google.com")  /*>  : browser.DocumentType = JsoupDocument(underlying = <!doctype html><html i…  */

val header = doc >?> text("#header")  /*>  : Option[String] = None  */
val body = doc >> text("body")  /*>  : String = "B\u00fasqueda Im\u00e1genes Maps Play YouTube Noticias Gmail Drive M\u00e1s \u…  */
