package soogle.es

import cats.effect._

import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.fields._
import com.sksamuel.elastic4s.Response
import com.sksamuel.elastic4s.requests.indexes.{
  CreateIndexResponse,
  IndexResponse
}
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
// import com.sksamuel.elastic4s.requests.analyzers._

object Es {
  import com.sksamuel.elastic4s.ElasticDsl._
  import soogle.data.Record

  val host = sys.env.getOrElse("ES_HOST", "127.0.0.1")
  val port = sys.env.getOrElse("ES_PORT", "9200")
  val clientProps = ElasticProperties(s"http://${host}:${port}")
  val esClient = ElasticClient(
    JavaClient(clientProps)
  ) // no side effecs here...

  val indexName = "methods"

  // TODO: create mapping if it doesn't exist
  def mapping[F[_]: Async](): F[Response[CreateIndexResponse]] =
    Async[F].fromFuture {
      Sync[F].delay {
        esClient.execute {
          createIndex(indexName).mapping(
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

  def recToIndexRequest(library: String, libraryVersion: String, rec: Record) =
    indexInto(indexName)
      .fields(
        "name" -> rec.name,
        "params" -> rec.params,
        "output" -> rec.output,
        "docString" -> rec.docString,
        "library" -> library,
        "version" -> libraryVersion
      )
      .refresh(RefreshPolicy.Immediate)

  def indexDoc[F[_]: Async](
      rec: Record,
      library: String,
      libraryVersion: String
  ): F[Response[IndexResponse]] =
    Async[F].fromFuture {
      Sync[F].delay {
        esClient.execute {
          // TODO: automate
          recToIndexRequest(library, libraryVersion, rec)
        }
      }
    }

  def bulkIndexDoc[F[_]: Async](
      rec: List[Record],
      library: String,
      libraryVersion: String
  // ): F[Response[BulkResponse]] =
  ) =
    Async[F].fromFuture {
      Sync[F].delay {
        esClient.execute {
          bulk {
            rec.map(recToIndexRequest(library, libraryVersion, _))
          }
        }
      }
    }

  // override def run(args: List[String]): IO[ExitCode] =
  //   mapping.as(ExitCode.Success)

}
