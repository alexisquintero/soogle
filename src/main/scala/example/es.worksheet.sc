import com.sksamuel.elastic4s.RequestSuccess
import com.sksamuel.elastic4s.RequestFailure
// name: Option[String]
// params: List[String]
// output: List[String]
// docString: Option[String]
// library: String
// library-javadoc: String
// po: params ++ output

// import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}

// val props = ElasticProperties("http://localhost:9200")
// val client = ElasticClient(JavaClient(props))

import com.sksamuel.elastic4s.fields.TextField
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.requests.searches.SearchResponse

object ArtistIndex extends App {
  val host = sys.env.getOrElse("ES_HOST", "127.0.0.1")
  val port = sys.env.getOrElse("ES_PORT", "9200")
  val clientProps = ElasticProperties(s"http:/${host}:${port}")
  val esClient = ElasticClient(JavaClient(clientProps))

  import com.sksamuel.elastic4s.ElasticDsl._

  esClient.execute {
    createIndex("artists").mapping(
      properties(
        TextField("name")
      )
    )
  }.await

  esClient.execute {
    indexInto("artists").fields("name" -> "L.S. Lowry").refresh(RefreshPolicy.Immediate)
  }

  val resp = esClient.execute {
    search("artists").query("lowry")
  }.await

  println("---- Search Results ----")
  resp match {
    case failure: RequestFailure => println("We failed " + failure.error)
    case results: RequestSuccess[SearchResponse] => println(results.result.hits.hits.toList)
    // case results: RequestSuccess[_] => println(results.result)
  }

  resp.foreach { search => println(s"There were ${search.totalHits} total hits") }

  esClient.close()
}
