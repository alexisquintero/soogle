// import soogle.es.Es
import soogle.data.Record

// import cats.effect._
// import cats.implicits._

val rec =
  Record(Option("name"), List("param1"), List("output1"), Option("docString"))  /*>  : Record = Record(name = Some(value = "nam…  */

// import cats.effect.unsafe.implicits.global

// Es.indexDoc(rec, "library", "1.0").unsafeRunSync()
