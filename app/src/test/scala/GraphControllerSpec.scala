package fr.scalaproject.app

import zio._
import zio.http._
import zio.test._
import zio.test.Assertion._
import zio.json._
import fr.scalaproject.core._
import fr.scalaproject.core.GraphSerialization._

object GraphControllerSpec extends DefaultRunnableSpec {

  def spec = suite("GraphControllerSpec")(

    testM("displayGraphController should return graph in DOT format") {
      val graphName = "testGraph"
      val graphInfo = GraphInformations(graphName, isWeighted = false, isBidirectional = false)
      val nodes = List(
        Node(1, List(Edge(2, 0))),
        Node(2, List(Edge(3, 0))),
        Node(3, List())
      )
      val graph = Graph(graphInfo, nodes)
      writeToFile(graph, graphName)

      val request = Request(Method.GET, URL(Path("/")).withQueryParams(Map("name" -> List(graphName))))
      val response = displayGraphController(request)

      for {
        body <- response.body.asString
      } yield assert(body.contains("digraph G"))(isTrue) &&
        assert(body.contains("1 -> 2"))(isTrue) &&
        assert(body.contains("2 -> 3"))(isTrue)
    },

    testM("addVertexController should add a vertex to the graph") {
      val graphName = "testGraph"
      val graphInfo = GraphInformations(graphName, isWeighted = false, isBidirectional = false)
      val nodes = List(
        Node(1, List(Edge(2, 0))),
        Node(2, List(Edge(3, 0))),
        Node(3, List())
      )
      val graph = Graph(graphInfo, nodes)
      writeToFile(graph, graphName)

      val request = Request(Method.GET, URL(Path("/")).withQueryParams(Map("name" -> List(graphName), "newNode" -> List("4"))))
      val response = addVertexController[Int](request)

      for {
        body <- response.body.asString
      } yield assert(body.contains("The graph has been successfully updated !"))(isTrue)
    },

    testM("removeVertexController should remove a vertex from the graph") {
      val graphName = "testGraph"
      val graphInfo = GraphInformations(graphName, isWeighted = false, isBidirectional = false)
      val nodes = List(
        Node(1, List(Edge(2, 0))),
        Node(2, List(Edge(3, 0))),
        Node(3, List())
      )
      val graph = Graph(graphInfo, nodes)
      writeToFile(graph, graphName)

      val request = Request(Method.GET, URL(Path("/")).withQueryParams(Map("name" -> List(graphName), "node" -> List("3"))))
      val response = removeVertexController[Int](request)

      for {
        body <- response.body.asString
      } yield assert(body.contains("The graph has been successfully updated !"))(isTrue)
    },

    testM("addEdgeController should add an edge to the graph") {
      val graphName = "testGraph"
      val graphInfo = GraphInformations(graphName, isWeighted = false, isBidirectional = false)
      val nodes = List(
        Node(1, List()),
        Node(2, List())
      )
      val graph = Graph(graphInfo, nodes)
      writeToFile(graph, graphName)

      val request = Request(Method.GET, URL(Path("/")).withQueryParams(Map("name" -> List(graphName), "startNode" -> List("1"), "endNode" -> List("2"), "weight" -> List("5"))))
      val response = addEdgeController[Int](request)

      for {
        body <- response.body.asString
      } yield assert(body.contains("The graph has been successfully updated !"))(isTrue)
    },

    testM("removeEdgeController should remove an edge from the graph") {
      val graphName = "testGraph"
      val graphInfo = GraphInformations(graphName, isWeighted = false, isBidirectional = false)
      val nodes = List(
        Node(1, List(Edge(2, 0))),
        Node(2, List(Edge(3, 0))),
        Node(3, List())
      )
      val graph = Graph(graphInfo, nodes)
      writeToFile(graph, graphName)

      val request = Request(Method.GET, URL(Path("/")).withQueryParams(Map("name" -> List(graphName), "startNode" -> List("1"), "endNode" -> List("2"))))
      val response = removeEdgeController[Int](request)

      for {
        body <- response.body.asString
      } yield assert(body.contains("The graph has been successfully updated !"))(isTrue)
    },

    testM("createGraphController should create a new graph") {
      val graphJson = """
        {
          "graphInformations": {
            "name": "newGraph",
            "isWeighted": false,
            "isBidirectional": false
          },
          "nodes": [
            {
              "id": 1,
              "edges": []
            },
            {
              "id": 2,
              "edges": []
            }
          ]
        }
      """

      val request = Request(Method.POST, URL(Path("/"))).withBody(graphJson)
      val response = createGraphController[Int](request)

      for {
        body <- response.body.asString
      } yield assert(body.contains("Graph successfully created"))(isTrue)
    }
  )
}
