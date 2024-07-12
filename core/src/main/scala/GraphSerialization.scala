package fr.scalaproject.core

import zio.json._
import scala.util.Using
import java.io._
import scala.io.Source
import scala.util._

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

    def fromJSON(json: String) : Either[String,Graph] = {
        json.fromJson[Graph]
    }

    def writeToFile(graph: Graph, graphName: String): Unit = {
        val json = toJSON(graph)
        Using(new PrintWriter(new File(s"graph/${graphName}.json"))) { writer =>
            writer.write(json)
        }.getOrElse(throw new RuntimeException("Failed to write to file"))
    }

    def readFromFile(graphName: String): Either[String, Graph] = {
        val source = Using(Source.fromFile(s"graph/${graphName}.json"))(_.mkString)
        source match {
            case Failure(value) => throw new IllegalStateException
            case Success(value) => fromJSON(value)
        }
    }

}
