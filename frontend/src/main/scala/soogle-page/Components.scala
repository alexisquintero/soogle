package sooglePage.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Search {

  def search: Callback =
    Callback.alert("Click")

  val searchInputs =
    ScalaComponent
      .builder[Unit]
      .renderStatic(
        <.div(
          <.input(^.tpe := "text", ^.placeholder := "Search for..."),
          <.button(^.onClick --> search, "Search")
        )
      )
      .build
}

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

object MainPage {
  val page =
    ScalaComponent
      .builder[Unit]
      .renderStatic(
        <.div(
          ^.padding := 12.px,
          ^.display := "flex",
          Sidebar.sidebar(),
          <.div(^.width := 100.pct, Search.searchInputs(), Welcome.welcome())
        )
      )
      .build
}
