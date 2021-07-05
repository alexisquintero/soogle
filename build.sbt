import Dependencies._

def sharedSettings(proyectName: String) = Seq(
  name := proyectName,
  version := "0.1",
  scalaVersion := "2.13.5"
)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(sharedSettings("shared"))
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % V.circeVersion,
      "io.circe" %%% "circe-generic" % V.circeVersion,
      "io.circe" %%% "circe-parser" % V.circeVersion
    )
  )
  .settings(scalacOptions ++= Seq("-Ymacro-annotations"))

lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js

lazy val scrapper = (project in file("scrapper"))
  .dependsOn(sharedJVM)
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
  .dependsOn(sharedJS)
  .settings(
    sharedSettings("frontend")
  )
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % V.scalajsDom,
      "com.github.japgolly.scalajs-react" %%% "core" % V.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % V.scalajsReact,
      "io.circe" %%% "circe-parser" % V.circeVersion
    ),
    Compile / npmDependencies ++= Seq(
      "react" -> "17.0.2",
      "react-dom" -> "17.0.2"
    ),
    webpackDevServerExtraArgs := Seq(
      "--host",
      "0.0.0.0",
      "--content-base",
      "/app/frontend/resources"
    )
  )
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
