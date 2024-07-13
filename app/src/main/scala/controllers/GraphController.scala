package fr.scalaproject.app

import zio._
import zio.http._
import fr.scalaproject.core._
import fr.scalaproject.core.GraphVisualization._
import fr.scalaproject.core.GraphSerialization._

def displayGraphController(req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile(finalGraphName) match {
            case Right(value) => Response.json(s"Here is the graph ${finalGraphName} :\n${graphToDOT(value)}")
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def addVertexController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val nodeToAdd = req.queryParam("newNode").map(_.toInt)
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (nodeToAdd.isEmpty) {
        Response.json("You must provide a node to add")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile(finalGraphName) match {
            case Right(value) => {
                val finalGraph = value.addVertex(nodeToAdd.getOrElse(0))
                GraphSerialization.writeToFile(finalGraph, finalGraphName)
                Response.json("The graph has been succesfully updated !")
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def removeVertexController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val nodeToRemove = req.queryParam("node").map(_.toInt)
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (nodeToRemove.isEmpty) {
        Response.json("You must provide a node to remove")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile(finalGraphName) match {
            case Right(value) => {
                val finalGraph = value.removeVertex(nodeToRemove.getOrElse(0))
                GraphSerialization.writeToFile(finalGraph, finalGraphName)
                Response.json("The graph has been succesfully updated !")
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def addEdgeController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val startNode = req.queryParam("startNode").map(_.toInt)
    val endNode = req.queryParam("endNode").map(_.toInt)
    val weight = req.queryParam("weight").map(_.toInt)
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (startNode.isEmpty) {
        Response.json("You must provide a valid start node !")
    } else if (endNode.isEmpty) {
        Response.json("You must provide a valid end node !")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile(finalGraphName) match {
            case Right(value) => {
                val finalGraph = value.addEdge(startNode.getOrElse(0), endNode.getOrElse(0), weight.getOrElse(0))
                GraphSerialization.writeToFile(finalGraph, finalGraphName)
                Response.json("The graph has been succesfully updated !")
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def removeEdgeController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val startNode = req.queryParam("startNode").map(_.toInt)
    val endNode = req.queryParam("endNode").map(_.toInt)
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (startNode.isEmpty) {
        Response.json("You must provide a valid start node !")
    } else if (endNode.isEmpty) {
        Response.json("You must provide a valid end node !")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile(finalGraphName) match {
            case Right(value) => {
                val finalGraph = value.removeEdge(startNode.getOrElse(0), endNode.getOrElse(0))
                GraphSerialization.writeToFile(finalGraph, finalGraphName)
                Response.json("The graph has been succesfully updated !")
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def createGraphController(req: Request) = {
    req.body.asString(Charsets.Utf8)
        .catchAll(_ => ZIO.succeed("Error reading request body"))
        .map(fromJSON)
        .map(graph => {
            graph match {
                case Left(value) => Response.json("Something went wrong !")
                case Right(value) => {
                    val graphName = value.graphInformations.name
                    writeToFile(value, graphName)
                    Response.json("Graph successfully created")
                }
            }
        })
}