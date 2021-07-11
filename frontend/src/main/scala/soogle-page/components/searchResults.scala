package sooglePage.components.searchResults

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object SearchResults {

  val searchResults =
    ScalaComponent
      .builder[Unit]
      .renderStatic(<.div("Results"))
      .build
}
