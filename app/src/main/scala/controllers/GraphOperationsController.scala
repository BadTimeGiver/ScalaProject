package fr.scalaproject.app

import zio.http._
import fr.scalaproject.core._
import fr.scalaproject.core.GraphOperations._

def bfsController(req: Request): Response = {
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

def dfsController(req: Request): Response = {
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

def hasCycleController (req: Request): Response = {
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
}

def topologicalSortController(req: Request): Response = {
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
}

def dijkstraController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val beginId = req.queryParam("beginId").map(_.toInt)

    if (graphName.isEmpty) {
       Response.json("You must provide a graph name")
    } else if (beginId.isEmpty) {
       Response.json("You must provide the ID of the begin node (name : beginId)")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile(finalGraphName) match {
            case Right(value) => Response.json(s"Result : ${dijkstra(value, beginId.getOrElse(0)).mkString(", ")}")
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def floydResultString(graph: Graph): String = {
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
        GraphSerialization.readFromFile(finalGraphName) match {
            case Right(value) => Response.json(s"Topological sort of the graph ${finalGraphName} :\n${floydResultString(value)}")
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

