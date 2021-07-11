package sooglePage

import org.scalajs.dom.document

import sooglePage.components.MainPage

object Page {

  def main(args: Array[String]): Unit = {
    MainPage.page().renderIntoDOM(document.getElementById("root"))
    ()
  }
}
