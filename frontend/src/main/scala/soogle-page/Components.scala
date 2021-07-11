package sooglePage.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
import japgolly.scalajs.react.vdom.html_<^._

import sooglePage.components.search.Search
import sooglePage.components.welcome.Welcome
import sooglePage.components.sidebar.Sidebar
import sooglePage.components.searchResults.SearchResults

import sooglePage.appState.AppState

object MainPage {

  final class Backend($ : BackendScope[Unit, AppState]) {
    private val setStateFn = StateSnapshot.withReuse.prepareVia($)

    def render(state: AppState): VdomElement = {
      val ss = setStateFn(state)

      <.div(
        ^.padding := 12.px,
        ^.display := "flex",
        Sidebar.sidebar(),
        <.div(
          ^.width := 100.pct,
          Search.searchInputs(ss),
          state.esResponse match {
            case None => Welcome.welcome()
            case Some(_) => SearchResults.searchResults()
          }
        )
      )
    }
  }

  val page = {
    ScalaComponent
      .builder[Unit]
      .initialState(AppState("", None))
      .renderBackend[Backend]
      .build
  }
}
