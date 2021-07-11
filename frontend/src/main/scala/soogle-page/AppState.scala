package sooglePage.appState

import monocle._
import japgolly.scalajs.react._

import shared.data.EsResponse

final case class AppState(search: String, esResponse: Option[EsResponse])

object AppState {
  val search = Lens[AppState, String](_.search)(s => _.copy(search = s))
  implicit val esResponseResusability =
    Reusability.caseClassExcept[EsResponse]("shards", "hits")
  implicit val reusability = Reusability.derive[AppState]
}
