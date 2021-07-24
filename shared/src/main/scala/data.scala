package shared.data

import io.circe._
import io.circe.generic.JsonCodec

@JsonCodec
final case class Shard(
    total: Int,
    successful: Int,
    skipped: Int,
    failed: Int
)

@JsonCodec
final case class Total(
    value: Int,
    relation: String
)

@JsonCodec
final case class Source(
    name: String,
    params: List[String],
    docString: Option[String],
    version: String,
    library: String,
    output: List[String],
    link: String
)

final case class InnerHit(
    index: String,
    hType: String,
    id: String,
    score: Int,
    source: Source
)

final object InnerHit {
  implicit val decodeInnerHit: Decoder[InnerHit] =
    Decoder.forProduct5("_index", "_type", "_id", "_score", "_source")(
      InnerHit.apply
    )
}

final case class Hit(
    total: Total,
    maxScore: Double,
    hits: List[InnerHit]
)

final object Hit {
  implicit val decodeHit: Decoder[Hit] =
    Decoder.forProduct3("total", "max_score", "hits")(Hit.apply)
}

final case class EsResponse(
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
