package sooglePage.components.sidebar

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Sidebar {
  val sidebar =
    ScalaComponent
      .builder[Unit]
      .renderStatic(
        <.a(
          ^.width := 160.px,
          ^.href := "https://github.com/alexisquintero/soogle",
          ^.target := "_blank",
          "Soogλe"
        )
      )
      .build
}
