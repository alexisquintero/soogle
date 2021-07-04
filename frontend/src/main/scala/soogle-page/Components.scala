package sooglePage.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
import japgolly.scalajs.react.vdom.html_<^._

import sooglePage.components.search.Search
import sooglePage.components.welcome.Welcome
import sooglePage.components.sidebar.Sidebar

object MainPage {

  final class Backend($ : BackendScope[Unit, String]) {
    private val setStateFn = StateSnapshot.withReuse.prepareVia($)

    def render(state: String): VdomElement = {
      val ss = setStateFn(state)

      <.div(
        ^.padding := 12.px,
        ^.display := "flex",
        Sidebar.sidebar(),
        <.div(
          ^.width := 100.pct,
          Search.searchInputs(ss),
          Welcome.welcome()
        )
      )
    }
  }

  val page = {
    ScalaComponent
      .builder[Unit]
      .initialState("")
      .renderBackend[Backend]
      .build
  }
}
