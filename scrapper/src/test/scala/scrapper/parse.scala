package soogleTest

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import cats.effect._
import fs2.io.file.Files
import fs2.io

import scala.io.Source

import java.nio.file.Paths

import soogle.scrapper.Scrapper

import shared.data.{Source => DataSource}

class ParseSpec extends AnyFlatSpec with Matchers {
  import cats.effect.unsafe.implicits.global
  val filename: String =
    "Scala Standard Library 2.13.6 - scala.collection.immutable.List.html"
  val file = Source.fromURL(getClass.getClassLoader.getResource(filename))
  "asd" should "qwe" in {
    val items = Files[IO]
      .readAll(Paths.get(s"scrapper/src/test/resources/${filename}"), 4096)
      .through(io.toInputStream)
      .flatMap { is =>
        Scrapper.isToSources[IO](is)
      }.compile
      .toList
      .unsafeRunSync()

    // TODO: continue
    items.length shouldEqual 385
    DataSource("", List(), None, "", "", List(), "") shouldEqual "datasource from map"
  }
}
