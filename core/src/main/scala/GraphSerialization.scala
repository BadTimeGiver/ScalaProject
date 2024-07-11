import zio.json._
import scala.util.Using
import java.io._

object GraphSerialization {
    implicit val edgeDecoder: JsonDecoder[Edge] = DeriveJsonDecoder.gen[Edge]
    implicit val edgeEncoder: JsonEncoder[Edge] = DeriveJsonEncoder.gen[Edge]

    implicit val nodeDecoder: JsonDecoder[Node] = DeriveJsonDecoder.gen[Node]
    implicit val nodeEncoder: JsonEncoder[Node] = DeriveJsonEncoder.gen[Node]

    implicit val graphInformationsDecoder: JsonDecoder[GraphInformations] = DeriveJsonDecoder.gen[GraphInformations]
    implicit val graphInformationsEncoder: JsonEncoder[GraphInformations] = DeriveJsonEncoder.gen[GraphInformations]

    implicit val graphDecoder: JsonDecoder[Graph] = DeriveJsonDecoder.gen[Graph]
    implicit val graphEncoder: JsonEncoder[Graph] = DeriveJsonEncoder.gen[Graph]

    def toJSON(graph: Graph): String ={
        graph.toJsonPretty
    }

    def fromJSON(json: String) : Either[String,Graph]={
        json.fromJson[Graph]
    }
    
    def writeToFile(graph: Graph, filePath: String): Unit = {
        val json = toJSON(graph)
        Using(new PrintWriter(new File(filePath))) { writer =>
            writer.write(json)
        }.getOrElse(throw new RuntimeException("Failed to write to file"))
    }
}