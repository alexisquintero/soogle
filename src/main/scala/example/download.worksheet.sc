import cats._
// import cats.data._
// import cats.implicits._
import cats.effect.{IO, IOApp, ExitCode, Async}
import fs2.Stream
import fs2.io
import fs2.io.file.Files

import java.net.URL
import java.io.InputStream
import java.nio.file.Paths

object docDownload extends IOApp {

  val docurl = new URL(
    "https://repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.4/scala-library-2.12.4-javadoc.jar"
  )

  def docUrl[F[_]: Applicative]: F[InputStream] =
    Applicative[F].pure(docurl.openConnection.getInputStream)

  def main[F[_]: Async](fileName: String): Stream[F, Unit] =
    io.readInputStream[F](docUrl[F], 4096)
      .through(Files[F].writeAll(Paths.get(fileName)))

  override def run(args: List[String]): IO[ExitCode] =
    main[IO]("scala-2-12-4.jar").compile.drain.as(ExitCode.Success)

}

// import cats.effect.unsafe.implicits.global

// docDownload.run(List()).unsafeRunSync()  /*>  : ExitCode = ExitCode(code = 0)  */
