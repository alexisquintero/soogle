import Dependencies._

ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

def sharedSettings(proyectName: String) = Seq(
  name := proyectName,
  version := "0.1",
  scalaVersion := "2.13.5"
)

lazy val scrapper = (project in file("scrapper"))
  .settings(sharedSettings("scrapper"))
  .settings(
    libraryDependencies ++= Seq(
      scalaTest % Test,
      scalaScrapper,
      fs2Core,
      fs2Io,
      catsEffects,
      elastic4s,
      elastic4sTest
    )
  )

lazy val frontend = (project in file("frontend"))
  .settings(
    sharedSettings("frontend")
  ).settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.1.0"
      /* scalajsDom */
    )
  )
  .enablePlugins(ScalaJSPlugin)
