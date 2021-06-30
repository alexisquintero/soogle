package soogle.main

import cats.effect._

import fs2.Stream

import soogle.scrapper.Scrapper
import soogle.es.Es
import soogle.docUnzip.DocUnzip

object Main extends IOApp {

  def main[F[_]: Async](libName: String, libVersion: String) =
    DocUnzip
      .getAllFiles[F](s"${libName}-${libVersion}")
      .filter { case (fileName, _) => fileName.endsWith("html") }
      .through(Scrapper.toRecs[F])
      .filter { _.nonEmpty }
      .flatMap { case recs =>
        Stream.eval {
          Es.bulkIndexDoc[F](recs, libName, libVersion)
        }
      }

  // get all files
  // make pipe to convert file to List[Record]
  // send to es
  override def run(args: List[String]): IO[ExitCode] =
    Es.mapping[IO]() >> main[IO]("scala", "2-12-4").compile.drain.as(ExitCode.Success)

}
