package fr.scalaproject.app

import zio.http._
import fr.scalaproject.core._
import fr.scalaproject.core.GraphOperations._
import fr.scalaproject.core.GraphSerialization._
import zio.json._

def bfsController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val beginId = req.queryParam("beginId")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (beginId.isEmpty) {
        Response.json("You must provide the ID of the begin node (name: beginId)")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(graph) => {
                val finalBeginNode = beginId.getOrElse("")
                if (!graph.hasNode(finalBeginNode)) Response.json("The begin node provided doesn't exist")
                else Response.json(bfs(graph, finalBeginNode).mkString(", "))
            }
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def dfsController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val beginId = req.queryParam("beginId")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (beginId.isEmpty) {
        Response.json("You must provide the ID of the begin node (name: beginId)")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(graph) => {
                val finalBeginNode = beginId.getOrElse("")
                if (!graph.hasNode(finalBeginNode)) Response.json("The begin node provided doesn't exist")
                else Response.json(dfs(graph, finalBeginNode).mkString(", "))
            }
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def hasCycleController(req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(graph) => Response.json(s"The graph ${finalGraphName} has a cycle: ${hasCycle(graph)}")
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def topologicalSortController(req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(graph) => Response.json(s"Topological sort of the graph ${finalGraphName} : ${topologicalSort(graph).mkString(", ")}")
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def dijkstraController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val beginId = req.queryParam("beginId")
    if (graphName.isEmpty) {
       Response.json("You must provide a graph name")
    } else if (beginId.isEmpty) {
       Response.json("You must provide the ID of the begin node (name: beginId)")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(graph) => {
                val finalBeginNode = beginId.getOrElse("")
                if (!graph.hasNode(finalBeginNode)) Response.json("The begin node provided doesn't exist")
                else Response.json(dijkstra(graph, finalBeginNode).mkString(", "))
            }
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}

def floydResultString(graph: Graph[String]): String = {
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

def floydWarshallController(req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(graph) => Response.json(s"Floyd-Warshall result for the graph ${finalGraphName} :\n${floydResultString(graph)}")
            case Left(_) => Response.json("The graph has not been found")
        }
    }
}