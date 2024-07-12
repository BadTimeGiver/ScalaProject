package fr.scalaproject.app

import fr.scalaproject.core._
import zio._
import zio.http._
import fr.scalaproject.core.GraphOperations._

object Application extends ZIOAppDefault {
    val routes = Routes(
        Method.GET / "bfs" -> handler { (req: Request) =>
            val graph = Graph(
                graphInformations = GraphInformations(
                    name = "TestGraph",
                    isWeighted = false,
                    isBidirectional = false
                ),
                nodes = List(
                    Node(id = 1, edges = List(Edge(to = 2, weight = 3))),
                    Node(id = 2, edges = List(Edge(to = 3, weight = 5))),
                    Node(id = 3, edges = List(Edge(to = 1, weight = 2)))
                )
            )
            Response.json(bfs(graph, 1).mkString(", "))
        },

        Method.GET / "dfs" -> handler { (req: Request) =>
            val graphName = req.queryParam("name")
            val beginId = req.queryParam("beginId").map(_.toInt)
            if (graphName.isEmpty) {
               Response.json("You must provide a graph name")
            } else if (beginId.isEmpty) {
               Response.json("You must provide the ID of the begin node (name : beginId)")
            } else {
                val finalGraphName = graphName.getOrElse("")
                GraphSerialization.readFromFile(finalGraphName) match {
                    case Right(value) => Response.json(dfs(value, beginId.getOrElse(0)).mkString(", "))
                    case Left(value) => Response.json("The graph has not been found")
                }
            }
        }
    )

    def run = Server.serve(routes).provide(Server.defaultWithPort(8084))
}