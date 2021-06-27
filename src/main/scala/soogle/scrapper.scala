package soogle.scrapper

import cats.implicits._
import cats.effect.Async

import fs2.{Stream, Pipe}

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model._

import java.io.InputStream

object Scrapper {
  import soogle.data.Record

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

    Record(name, params, output, docString)
  }

  def isToRecords(is: InputStream): List[Record] = {
    val doc = JsoupBrowser().parseInputStream(is)
    val elems: List[Element] = (doc >?> elementList(
      "div.values.members"
    )).toList.flatten

    elems
      .filter { e => e >?> text("h3") == Option("Concrete Value Members") }
      .flatMap { ce => ce >?> elementList("ol li") }
      .flatten
      .map(liToRecord)
  }

  def toRecs[F[_]: Async]: Pipe[F, (String, Stream[F, Byte]), List[Record]] = {
    docFiles: Stream[F, (String, Stream[F, Byte])] =>
      docFiles.flatMap { case (_, sis) => // TODO: do smtg with file name?
        sis.through(fs2.io.toInputStream).flatMap { is =>
          Stream {
            isToRecords(is)
          }
        }
      }
  }

}
