package fr.scalaproject.core

import zio.json._
import scala.util.Using
import java.io._
import scala.io.Source
import scala.util._

object GraphSerialization {
    implicit def edgeDecoder[T: JsonDecoder]: JsonDecoder[Edge[T]] = DeriveJsonDecoder.gen[Edge[T]]
    implicit def edgeEncoder[T: JsonEncoder]: JsonEncoder[Edge[T]] = DeriveJsonEncoder.gen[Edge[T]]

    implicit def nodeDecoder[T: JsonDecoder]: JsonDecoder[Node[T]] = DeriveJsonDecoder.gen[Node[T]]
    implicit def nodeEncoder[T: JsonEncoder]: JsonEncoder[Node[T]] = DeriveJsonEncoder.gen[Node[T]]

    implicit val graphInformationsDecoder: JsonDecoder[GraphInformations] = DeriveJsonDecoder.gen[GraphInformations]
    implicit val graphInformationsEncoder: JsonEncoder[GraphInformations] = DeriveJsonEncoder.gen[GraphInformations]

    implicit def graphDecoder[T: JsonDecoder]: JsonDecoder[Graph[T]] = DeriveJsonDecoder.gen[Graph[T]]
    implicit def graphEncoder[T: JsonEncoder]: JsonEncoder[Graph[T]] = DeriveJsonEncoder.gen[Graph[T]]

    def toJSON[T: JsonEncoder](graph: Graph[T]): String = {
        graph.toJsonPretty
    }

    def fromJSON[T: JsonDecoder](json: String): Either[String, Graph[T]] = {
        json.fromJson[Graph[T]]
    }

    def writeToFile[T: JsonEncoder](graph: Graph[T], graphName: String): Unit = {
        val json = toJSON(graph)
        Using(new PrintWriter(new File(s"graph/${graphName}.json"))) { writer =>
            writer.write(json)
        }.getOrElse(throw new RuntimeException("Failed to write to file"))
    }

    def readFromFile[T: JsonDecoder](graphName: String): Either[String, Graph[T]] = {
        val source = Using(Source.fromFile(s"graph/${graphName}.json"))(_.mkString)
        source match {
            case Failure(_) => Left("Graph not found")
            case Success(value) => fromJSON(value)
        }
    }
}
