package sooglePage.components.welcome

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Welcome {

  val example =
    ScalaComponent
      .builder[Unit]
      .renderStatic(
        <.div(
          ^.border := "1px solid black",
          ^.width := 400.px,
          ^.margin := "0 auto",
          <.h2(
            ^.margin := 0.px,
            "Example searches:"
          ),
          <.a(^.href := "?soogle=map", ^.marginLeft := 20.px, "Map"),
          <.p(
            ^.marginBottom := 0.px,
            "Enter your own search at the top of the page"
          )
        )
      )
      .build

  val welcome =
    ScalaComponent
      .builder[Unit]
      .renderStatic(
        <.div(
          <.h1("Welcome to Soogle"),
          <.p(
            "Soogle tries to mimic ",
            <.a(
              ^.href := "https://hoogle.haskell.org/",
              ^.target := "_blank",
              "Hoogle"
            ),
            "'s functionality."
          ),
          example()
        )
      )
      .build
}
