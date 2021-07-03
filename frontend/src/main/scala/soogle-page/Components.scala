package sooglePage.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Search {

  val searchInputs =
    ScalaComponent
      .builder[Unit]
      .renderStatic(<.div("search"))
      .build
}

object Welcome {
  val welcome =
    ScalaComponent
      .builder[Unit]
      .renderStatic(<.div("welcome"))
      .build
}

object Sidebar {
  val sidebar =
    ScalaComponent
      .builder[Unit]
      .renderStatic(<.div("sidebar"))
      .build
}

object MainPage {
  val page =
    ScalaComponent
      .builder[Unit]
      .renderStatic(
        <.div(Sidebar.sidebar(), Search.searchInputs(), Welcome.welcome())
      )
      .build
}
