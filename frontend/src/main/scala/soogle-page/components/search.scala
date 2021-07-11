package sooglePage.components.search

import io.circe.parser._
import io.circe
// import monocle._
// import monocle.syntax.all._

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
import japgolly.scalajs.react.vdom.html_<^._
// import japgolly.scalajs.react.MonocleReact._

import sooglePage.appState.AppState
import shared.data._

object Search {

  final class Backend($ : BackendScope[AppState, AppState]) {

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
        val parsedRes: circe.Error Either EsResponse =
          parse(xhr.responseText).flatMap(_.as[EsResponse])

        parsedRes match {
          case Right(value) =>
            Callback($.modState(_.copy(esResponse = Option(value))).runNow())
          // case Right(value) => $.modState(_.copy(esResponse = Option(value)))
          case Left(_) => Callback.alert("Error parsing response")
          // case Left(_) => Callback($.modState(_.copy(esResponse = None)).runNow())
        }
      }

    // TODO: Use StateSnapshot String
    def render(s: StateSnapshot[AppState]) = {
      <.div(
        <.input(
          ^.tpe := "text",
          ^.placeholder := "Search for...",
          ^.value := s.value.search,
          ^.onChange ==> ((e: ReactEventFromInput) =>
            $.modState(_.copy(search = e.target.value))
          )
        ),
        <.button(
          ^.onClick --> ajax.asCallback,
          "Search"
        )
      )
    }
  }

  val searchInputs =
    ScalaComponent
      .builder[StateSnapshot[AppState]]
      // .renderBackend[Backend]
      .render_P { s =>
        val ajax = Ajax //TODO: move to backend
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
      """).onComplete { xhr =>
          val parsedRes: circe.Error Either EsResponse =
            parse(xhr.responseText).flatMap(_.as[EsResponse])

          parsedRes match {
            case Right(value) =>
              s.modState(_.copy(esResponse = Option(value)))
            case Left(_) => Callback.alert("Error parsing response")
          }
        }

        <.div(
          <.input(
            ^.tpe := "text",
            ^.placeholder := "Search for...",
            ^.value := s.value.search,
            ^.onChange ==> ((e: ReactEventFromInput) =>
              s.modState(_.copy(search = e.target.value))
            )
          ),
          <.button(
            ^.onClick --> ajax.asCallback,
            "Search"
          )
        )
      }
      .build
}
