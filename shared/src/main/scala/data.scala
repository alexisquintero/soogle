package shared.data

import io.circe._
import io.circe.generic.JsonCodec

@JsonCodec
case class Shard(
    total: Int,
    successful: Int,
    skipped: Int,
    failed: Int
)

@JsonCodec
case class Total(
    value: Int,
    relation: String
)

// TODO: use shared Record
@JsonCodec
case class Source(
    name: Option[List[String]], // TODO: fix
    params: List[String],
    docString: Option[String],
    version: String,
    library: String,
    output: List[String]
)

case class InnerHit(
    index: String,
    hType: String,
    id: String,
    score: Int,
    source: Source
)

object InnerHit {
  implicit val decodeInnerHit: Decoder[InnerHit] =
    Decoder.forProduct5("_index", "_type", "_id", "_score", "_source")(
      InnerHit.apply
    )
}

case class Hit(
    total: Total,
    maxScore: Double,
    hits: List[InnerHit]
)

object Hit {
  implicit val decodeHit: Decoder[Hit] =
    Decoder.forProduct3("total", "max_score", "hits")(Hit.apply)
}

case class EsResponse(
    took: Int,
    timedOut: Boolean,
    shards: Shard,
    hits: Hit
)

object EsResponse {
  implicit val decodeEsResponse: Decoder[EsResponse] =
    Decoder.forProduct4("took", "timed_out", "_shards", "hits")(
      EsResponse.apply
    )
}
