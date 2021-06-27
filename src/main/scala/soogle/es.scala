package soogle.es

import cats.effect._

import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.fields._
import com.sksamuel.elastic4s.Response
import com.sksamuel.elastic4s.requests.indexes.CreateIndexResponse
// import com.sksamuel.elastic4s.requests.analyzers._

object es extends IOApp {
  val host = sys.env.getOrElse("ES_HOST", "127.0.0.1")
  val port = sys.env.getOrElse("ES_PORT", "9200")
  val clientProps = ElasticProperties(s"http://${host}:${port}")
  val esClient = ElasticClient(JavaClient(clientProps))

  import com.sksamuel.elastic4s.ElasticDsl._

  val index = "methods"

  // TODO: create mapping if it doesn't exist
  val mapping: IO[Response[CreateIndexResponse]] = IO.fromFuture {
    IO.delay {
      esClient.execute {
        createIndex(index).mapping(
          properties(
            TextField("name"), // lowercase
            TextField("params"), // lowercase
            TextField("output"), // lowercase
            TextField("docString"), // raw
            TextField("library"), // keyword
            TextField("version") // keyword
            // TextField("paramsAndOutput"), // TODO: order important; from params ++ output
          )
        )
      }
    }
  }

  override def run(args: List[String]): IO[ExitCode] =
    mapping.as(ExitCode.Success)

}
