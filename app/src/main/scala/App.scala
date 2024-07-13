package fr.scalaproject.app

import fr.scalaproject.core._
import zio._
import zio.http._
import fr.scalaproject.core.GraphOperations._

object Application extends ZIOAppDefault {
    val routes = Routes(
        Method.GET / "bfs" -> handler { (req: Request) =>
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
        },

        Method.GET / "has_cycle" -> handler { (req: Request) => {
            val graphName = req.queryParam("name")
            if (graphName.isEmpty) {
                Response.json("You must provide a graph name")
            } else {
                val finalGraphName = graphName.getOrElse("")
                GraphSerialization.readFromFile(finalGraphName) match {
                    case Right(value) => Response.json(s"The graph ${finalGraphName} has a cycle : ${hasCycle(value)}")
                    case Left(value) => Response.json("The graph has not been found")
                }
            }
        } },

        Method.GET / "topological-sort" -> handler { (req: Request) => {
            val graphName = req.queryParam("name")
            if (graphName.isEmpty) {
                Response.json("You must provide a graph name")
            } else {
                val finalGraphName = graphName.getOrElse("")
                GraphSerialization.readFromFile(finalGraphName) match {
                    case Right(value) => Response.json(s"Topological sort of the graph ${finalGraphName} : ${topologicalSort(value).mkString(", ")}")
                    case Left(value) => Response.json("The graph has not been found")
                }
            }
        } }
    )

    def run = Server.serve(routes).provide(Server.defaultWithPort(8084))
}