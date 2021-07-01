import sbt._
// import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
// import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object V {
  val fs2 = "3.0.0"
  val cats = "3.1.1"
  val elastic4sVersion = "7.12.3"

  val scalajsDom = "1.1.0"
  val scalajsReact = "1.7.7"
}

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8"
  lazy val scalaScrapper = "net.ruippeixotog" %% "scala-scraper" % "2.2.1"
  lazy val fs2Core = "co.fs2" %% "fs2-core" % V.fs2
  lazy val fs2Io = "co.fs2" %% "fs2-io" % V.fs2
  lazy val catsEffects = "org.typelevel" %% "cats-effect" % V.cats
  lazy val elastic4s = "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % V.elastic4sVersion
  lazy val elastic4sTest = "com.sksamuel.elastic4s" %% "elastic4s-testkit" % V.elastic4sVersion % "test"

  // lazy val scalajsDom = "org.scala-js" %%% "scalajs-dom" % V.scalajsDom
}

