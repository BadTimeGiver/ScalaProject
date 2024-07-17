package fr.scalaproject.app

import zio.http._
import fr.scalaproject.core._
import fr.scalaproject.core.GraphOperations._
import fr.scalaproject.core.GraphSerialization._
import zio.json._

def bfsController[T: JsonDecoder : JsonEncoder : Ordering](req: Request): Response = {
    val graphName = req.queryParam("name")
    val beginId = req.queryParam("beginId").map(_.asInstanceOf[T])
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (beginId.isEmpty) {
        Response.json("You must provide the ID of the begin node (name: beginId)")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[T](finalGraphName) match {
            case Right(graph) => {
                val finalBeginNode = beginId.getOrElse(throw new RuntimeException("Invalid node ID"))
                if (!graph.hasNode(finalBeginNode)) Response.json("The begin node provided doesn't exist")
                else Response.json(bfs(graph, finalBeginNode).mkString(", "))
            }
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def dfsController[T: JsonDecoder : JsonEncoder : Ordering](req: Request): Response = {
    val graphName = req.queryParam("name")
    val beginId = req.queryParam("beginId").map(_.asInstanceOf[T])
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (beginId.isEmpty) {
        Response.json("You must provide the ID of the begin node (name: beginId)")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[T](finalGraphName) match {
            case Right(graph) => {
                val finalBeginNode = beginId.getOrElse(throw new RuntimeException("Invalid node ID"))
                if (!graph.hasNode(finalBeginNode)) Response.json("The begin node provided doesn't exist")
                else Response.json(dfs(graph, finalBeginNode).mkString(", "))
            }
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def hasCycleController[T: JsonDecoder : JsonEncoder : Ordering](req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[T](finalGraphName) match {
            case Right(graph) => Response.json(s"The graph ${finalGraphName} has a cycle: ${hasCycle(graph)}")
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def topologicalSortController[T: JsonDecoder : JsonEncoder : Ordering](req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[T](finalGraphName) match {
            case Right(graph) => Response.json(s"Topological sort of the graph ${finalGraphName} : ${topologicalSort(graph).mkString(", ")}")
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def dijkstraController[T: JsonDecoder : JsonEncoder : Ordering](req: Request): Response = {
    val graphName = req.queryParam("name")
    val beginId = req.queryParam("beginId").map(_.asInstanceOf[T])

    if (graphName.isEmpty) {
       Response.json("You must provide a graph name")
    } else if (beginId.isEmpty) {
       Response.json("You must provide the ID of the begin node (name: beginId)")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[T](finalGraphName) match {
            case Right(graph) => {
                val finalBeginNode = beginId.getOrElse(throw new RuntimeException("Invalid node ID"))
                if (!graph.hasNode(finalBeginNode)) Response.json("The begin node provided doesn't exist")
                else Response.json(dijkstra(graph, finalBeginNode).mkString(", "))
            }
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def floydResultString[T: Ordering](graph: Graph[T]): String = {
    val result = floyd(graph)
    val sb = new StringBuilder
    result.foreach { case ((i, j), distance) =>
        if (distance == Int.MaxValue) {
            sb.append(s"Distance from $i to $j: ∞\n")
        } else {
            sb.append(s"Distance from $i to $j: $distance\n")
        }
    }
    sb.toString()
}

def floydWarshallController[T: JsonDecoder : JsonEncoder : Ordering](req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[T](finalGraphName) match {
            case Right(graph) => Response.json(s"Floyd-Warshall result for the graph ${finalGraphName} :\n${floydResultString(graph)}")
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}