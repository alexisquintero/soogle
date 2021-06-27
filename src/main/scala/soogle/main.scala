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
      .through(Scrapper.toRecs[F])
      .flatMap { case recs =>
        Stream {
          recs.map { rec =>
            Es.indexDoc(rec, libName, libVersion)
          }
        }
      }

  // get all files
  // make pipe to convert file to List[Record]
  // send to es
  override def run(args: List[String]): IO[ExitCode] =
    Es.mapping >> main[IO]("scala", "2-12-4").compile.drain.as(ExitCode.Success)

}
