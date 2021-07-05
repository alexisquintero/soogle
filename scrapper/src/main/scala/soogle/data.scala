package soogle.data

case class Record(
    name: Option[String],
    params: List[String],
    output: List[String],
    docString: Option[String]
)

