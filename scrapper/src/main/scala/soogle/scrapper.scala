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
  import shared.data.Source

  def liToSource(li: Element): Source = {

    val permalink: String =
      li >> element("span.permalink") >> attr("href")("a") // TODO: better type
    val symbol: Element = li >> element("span.symbol")

    val name = symbol >> text("span.name")

    def processExtype(spanClass: String): List[String] = (for {
      sParams <- symbol >?> elementList(s"span.${spanClass}")
      sNames <- sParams.map { sp =>
        sp >?> attr("name")("span.extype")
      }.sequence
    } yield sNames).toList.flatten

    val params: List[String] = processExtype("params")

    val output: List[String] = processExtype("result")

    val docString: Option[String] = symbol >?> text("p.shortcomment")

    Source(name, params, docString, "", "", output, permalink) // TODO: fix ""
  }

  def isToSources[F[_]](is: InputStream): Stream[F, Source] = {
    val doc = JsoupBrowser().parseInputStream(is)
    val elems: Stream[F, Element] = Stream.emits {
      (doc >?> elementList("div.values.members")).toList.flatten
    }

    elems
      .filter { e => e >?> text("h3") == Option("Value Members") }
      .flatMap { ce =>
        Stream.emits {
          (ce >?> elementList("ol li")).toList.flatten
        }
      }
      .filter { pre =>
        val permalink = pre >?> element("span.permalink")
        val symbol = pre >?> element("span.symbol")
        val name = symbol.flatMap { s => s >?> text("span.name") }

        name != None && permalink != None && symbol != None
      }
      .map(liToSource)
  }

  def toRecs[F[_]: Async]: Pipe[F, (String, Stream[F, Byte]), Source] = {
    docFiles: Stream[F, (String, Stream[F, Byte])] =>
      docFiles.flatMap { case (_, sis) =>
        sis.through(fs2.io.toInputStream).flatMap { is =>
          isToSources[F](is)
        }
      }
  }

}
