package sooglePage.components.searchResults

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import shared.data._

object SingleSearchResult {
  val singleSearchResult =
    ScalaComponent
      .builder[InnerHit]
      .render_P { case InnerHit(_, _, _, _, source) =>
        <.div(
          ^.key := List(source.name)
            .concat(source.params)
            .concat(source.output)
            .mkString,
          ^.classSet("result" -> true),
          ^.marginTop := 15.px,
          <.div(
            ^.classSet("ans" -> true),
            <.a(
              ^.href := "",
              ^.display := "block",
              ^.width := 100.pct,
              ^.backgroundColor := "lightgray",
              <.span(
                ^.classSet("name" -> true),
                ^.display := "block",
                source.name
              ),
              source.params.concat(source.output).mkString(" -> ")
            )
          ),
          <.div(
            ^.classSet("from" -> true),
            ^.marginTop := 5.px,
            source.library.appendedAll(source.version)
          ),
          <.div(
            ^.classSet("doc" -> true),
            source.docString.getOrElse[String]("")
          )
        )
      }
      .build
}

object SearchResults {
  import SingleSearchResult._

  val searchResults =
    ScalaComponent
      .builder[Hit]
      .render_P { case Hit(_, _, hits) =>
        <.div(
          hits.toVdomArray(singleSearchResult(_))
        )
      }
      .build
}
