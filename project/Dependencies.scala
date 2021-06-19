import sbt._

object V {
  val fs2 = "3.0.0"
}

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8"
  lazy val scalaScrapper = "net.ruippeixotog" %% "scala-scraper" % "2.2.1"
  lazy val fs2Core = "co.fs2" %% "fs2-core" % V.fs2
  lazy val fs2Io = "co.fs2" %% "fs2-io" % V.fs2
}
