// https://gist.github.com/nmehitabel/a7c976ef8f0a41dfef88e981b9141075

import cats.effect._
import cats.data._
// import cats.implicits._
import fs2.Stream
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

  def unzip[F[_]: Async] = { zipped: Stream[F, Byte] =>
    zipped.through(io.toInputStream).flatMap { is: InputStream =>
      val zis: F[ZipInputStream] = Sync[F].delay(new ZipInputStream(is))
      val bzis: Stream[F, ZipInputStream] = Stream.bracket(zis) { bzis =>
        Sync[F].delay(bzis.close())
      }
      bzis.flatMap(unzipEntries[F])
    }
  }

  def main[F[_]: Async](fileName: String): Stream[F, Unit] =
    Files[F].readAll(Paths.get(fileName), 4096).through(unzip).flatMap {
      case (entry, is) =>
        val fullPath: Path = Paths.get(s"${fileName}/${entry}")
        Files[F].createDirectories(fullPath)
        is.through(Files[F].writeAll(fullPath))
    }

  override def run(args: List[String]): IO[ExitCode] = {
    main[IO]("scala-2-12-4.jar").compile.drain.as(ExitCode.Success)
  }

}

// object asd extends IOApp {

//   override def run(args: List[String]): IO[ExitCode] =
//     Files[IO]
//       .readAll(Paths.get("scala-2-12-4.jar"), 4096)
//       .compile
//       .drain
//       .as(ExitCode.Success)
// }

// import cats.effect.unsafe.implicits.global

// asd.run(List()).unsafeRunSync() /*>  : ExitCode = ExitCode(code = 0)  */
// val a = Files[IO].readAll(Paths.get("scala-2-12-4.jar"), 4096).compile.drain.unsafeRunSync()

// docUnzip.run(List()).unsafeRunSync()

Option(Paths.get("qwe/asd").getParent)  /*>  : Option[Path] = Some(value = qwe)  */
Option(Paths.get("asd").getParent)  /*  /*>  : Option[Path] = Some(value = qwe)  */>  : Option[Path] = None  */
  /*>  : Option[Path] = None  */
