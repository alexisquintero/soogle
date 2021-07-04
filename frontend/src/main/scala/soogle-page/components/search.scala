package sooglePage.components.search

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
import japgolly.scalajs.react.vdom.html_<^._

object Search {

  def search(s: String): Callback =
    Callback.alert(s)

  val searchInputs =
    ScalaComponent
      .builder[StateSnapshot[String]]
      .render_P { stateSnapshot =>
        <.div(
          <.input(
            ^.tpe := "text",
            ^.placeholder := "Search for...",
            ^.value := stateSnapshot.value,
            ^.onChange ==> ((e: ReactEventFromInput) =>
              stateSnapshot.setState(e.target.value)
            )
          ),
          <.button(^.onClick --> search(stateSnapshot.value), "Search")
        )
      }
      .build
}
