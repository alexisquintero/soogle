package soogle.scrapper

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model._
import cats.implicits._

case class Record(
    name: Option[String],
    params: List[String],
    output: List[String],
    docString: Option[String],
    library: String
)

object Scrapper {
  def liToRecord(li: Element): Record = {

    val symbol: Option[Element] = li >?> element("span.symbol")

    val name: Option[String] = symbol.flatMap { s => s >?> text("span.name") }

    def processExtype(spanClass: String): List[String] = (for {
      s <- symbol
      sParams <- s >?> elementList(s"span.${spanClass}")
      sNames <- sParams.map { sp =>
        sp >?> attr("name")("span.extype")
      }.sequence
    } yield sNames).toList.flatten

    val params: List[String] = processExtype("params")

    val output: List[String] = processExtype("result")

    val docString: Option[String] = symbol.flatMap { s =>
      s >?> text("p.shortcomment")
    }

    val library: String = ""

    Record(name, params, output, docString, library)
  }

  def urlToRecords(url: String): List[Record] = {
    val elems: List[Element] = (JsoupBrowser().get(url) >?> elementList(
      "div.values.members"
    )).toList.flatten

    elems
      .filter { e => e >?> text("h3") == Option("Concrete Value Members") }
      .flatMap { ce => ce >?> elementList("ol li") }
      .flatten
      .map(liToRecord)
  }

}
