package sooglePage.components.search

import io.circe.parser._

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
import japgolly.scalajs.react.vdom.html_<^._

import shared.data._

object Search {

  def search(s: String): Callback = {
    println(s)

    val ajax = Ajax
      .get(
        "http://localhost:9200/methods/_search"
      )
      .setRequestContentTypeJsonUtf8
      .send("""
      {
        "query": {
          "match_all": {}
        }
      }
      """)
      .onComplete { xhr =>
        Callback.alert {
          print(parse(xhr.responseText).flatMap(_.as[EsResponse]))
          "@@qwe"
        }
      }
      .asCallback

    ajax
  }

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
