import Dependencies._

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
  )
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % V.scalajsDom,
      "com.github.japgolly.scalajs-react" %%% "core" % V.scalajsReact
    ),
    npmDependencies in Compile ++= Seq(
      "react" -> "17.0.2",
      "react-dom" -> "17.0.2"
    )
  )
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
