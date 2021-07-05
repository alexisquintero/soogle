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

  def processExtype(spanClass: String): List[String] = (for {
    s <- symbol
    sParams <- s >?> elementList(s"span.${spanClass}")
    sNames <- sParams.map { sp => sp >?> attr("name")("span.extype") }.sequence
  } yield sNames).toList.flatten

  val params: List[String] = processExtype("params")
  val output: List[String] = processExtype("result")
  val docString: Option[String] = symbol.flatMap { s => s >?> text("p.shortcomment") }
  val library: String = ""
  Record(name, params, output, docString, library)
}

val browser = JsoupBrowser()
val doc = browser.get("https://www.scala-lang.org/api/2.13.6/scala/math/Numeric.html")

val header = doc >?> text("#header")
val body = doc >> text("body")

val elems = doc >?> elementList("div.values.members")
// remove style display none
val conc = elems
            .toList
            .flatten
            .filter { e => e >?> text("h3") == Option("Concrete Value Members") }
            .flatMap { ce => ce >?> elementList("ol li") }
            .flatten
            .map(liToRecord)

conc.tail.tail.tail.tail.tail.head
