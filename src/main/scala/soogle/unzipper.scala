package soogle.scrapper

// https://gist.github.com/nmehitabel/a7c976ef8f0a41dfef88e981b9141075

import cats.effect._
import cats.data._
// import cats.implicits._
import fs2.{Stream, Pipe}
import fs2.io
import fs2.io.file.Files

import java.io.InputStream
import java.nio.file.{Path, Paths}
import java.util.zip._

object docUnzip extends IOApp {

  def entry[F[_]: Sync](
      zis: ZipInputStream
  ): OptionT[F, (String, Stream[F, Byte])] =
    OptionT(Sync[F].delay(Option(zis.getNextEntry))).map { ze =>
      (ze.getName, io.readInputStream(Sync[F].delay(zis), 4096, false))
    }

  def unzipEntries[F[_]: Sync](
      zis: ZipInputStream
  ): Stream[F, (String, Stream[F, Byte])] =
    Stream.unfoldEval(zis) { szis =>
      entry[F](szis).map((_, szis)).value
    }

  def unzip[F[_]: Async]: Pipe[F, Byte, (String, Stream[F, Byte])] = {
    zipped: Stream[F, Byte] =>
      zipped.through(io.toInputStream).flatMap { is: InputStream =>
        val zis: F[ZipInputStream] = Sync[F].delay(new ZipInputStream(is))
        val bzis: Stream[F, ZipInputStream] = Stream.bracket(zis) { bzis =>
          Sync[F].delay(bzis.close())
        }
        bzis.flatMap(unzipEntries[F])
      }
  }

  def main[F[_]: Async](fileName: String): Stream[F, Unit] =
    Files[F]
      .readAll(Paths.get(s"${fileName}.jar"), 4096)
      .through(unzip).flatMap { case (entry, is) =>
        val fullPath: Path = Paths.get(s"${fileName}/${entry}")
        is.through(Files[F].writeAll(fullPath))
      }

  override def run(args: List[String]): IO[ExitCode] =
    main[IO]("scala-2-12-4").compile.drain.as(ExitCode.Success)

}
