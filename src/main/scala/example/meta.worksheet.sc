import net.ruippeixotog.scalascraper.browser.JsoupBrowser
// import net.ruippeixotog.scalascraper.browser.JsoupBrowser.JsoupElement
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
// import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model._
// import cats._
import cats.data._
import cats.implicits._


// Use specific type for params and output
case class Record(name: Option[String], params: List[String], output: List[String], docString: Option[String], library: String)

def liToRecord(li: Element): Record = {
  val symbol: Option[Element] = li >?> element("span.symbol")
  val name: Option[String] = symbol.flatMap { s => s  >?> text("span.name") }
  // def processExtype = ???
  val params: List[String] = (for {
    s <- symbol
    sParams <- s >?> elementList("span.params")
    sNames <- sParams.map { sp => sp >?> attr("name")("span.extype") }.sequence
  } yield sNames).toList.flatten
  val docString: Option[String] = symbol.flatMap { s => s >?> text("p.shortcomment") }
  val library: String = ""
  Record(name, params, List(), docString, library)
}

val browser = JsoupBrowser()  /*>  : net.ruippeixotog.scalascraper.browser.Browser = net.ruippeixotog.scalascraper.browser.Jsou…  */
val doc = browser.get("https://www.scala-lang.org/api/2.13.6/scala/math/Numeric.html")  /*>  : browser.DocumentType = JsoupDocu…  */

val header = doc >?> text("#header")  /*>  : Option[String] = None  */
val body = doc >> text("body")  /*>  : String = """Scala Standard Library2.13.6 < Back  Packages  package root This is the d…  */

val elems = doc >?> elementList("div.values.members")  /*>  : Option[List[Element]] = Some(value = List(JsoupElement(underlying…  */
// remove style display none
val conc = elems
            .toList
            .flatten  /*>  : List[net.ruippeixotog.scalascraper.model.Element] = List(JsoupElement(underlying = <div class="val…  */
            .filter { e => e >?> text("h3") == Option("Concrete Value Members") }  /*>  : List[net.ruippeixotog.scalascraper.mod…  */
            .flatMap { ce => ce >?> elementList("ol li") }    /*>  : List[List[net.ruippeixotog.scalascraper.model.Element]] = List(…  */  /*>  : List[List[Element]…  */
            .flatten  /*>  : List[Element] = List(JsoupElement(underlying = <li class="indented0 " name="scala.AnyRef#!=" group…  */
            .map(liToRecord)  /*>  : List[Record] = List(Record(name = Some(value = "!="),params = List(),output = List(),docSt…  */

conc.tail.tail.tail.tail.tail.head  /*>  : Record = Record(name = Some(value = "abs"),params = List("scala.math.Numeric.T"),out…  */
