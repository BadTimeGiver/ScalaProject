package fr.scalaproject.app

import zio._
import zio.http._
import fr.scalaproject.core._
import fr.scalaproject.core.GraphVisualization._
import fr.scalaproject.core.GraphSerialization._
import zio.json._

def displayGraphController(req: Request): Response = {
    val graphName = req.queryParam("name")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(value) => Response.json(s"Here is the graph ${finalGraphName} :\n${value.toDOT}")
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def addVertexController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val nodeToAdd = req.queryParam("newNode")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (nodeToAdd.isEmpty) {
        Response.json("You must provide a node to add")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(value) => {
                val finalNodeToAdd = nodeToAdd.getOrElse("")
                if(value.hasNode(finalNodeToAdd)) {
                    Response.json("This node already exists in the graph !")
                } else {
                    val finalGraph = value.addVertex(finalNodeToAdd)
                    GraphSerialization.writeToFile(finalGraph, finalGraphName)
                    Response.json("The graph has been succesfully updated !")
                }
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def removeVertexController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val nodeToRemove = req.queryParam("node")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (nodeToRemove.isEmpty) {
        Response.json("You must provide a node to remove")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(value) => {
                val finalNodeToRemove = nodeToRemove.getOrElse("")
                if(value.hasNode(finalNodeToRemove)) {
                    val finalGraph = value.removeVertex(finalNodeToRemove)
                    GraphSerialization.writeToFile(finalGraph, finalGraphName)
                    Response.json("The graph has been succesfully updated !")
                } else {
                    Response.json("The graph does not have the vertex to remove !")
                }
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def addEdgeController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val startNode = req.queryParam("startNode")
    val endNode = req.queryParam("endNode")
    val weight = req.queryParam("weight").map(_.toInt)
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (startNode.isEmpty) {
        Response.json("You must provide a valid start node !")
    } else if (endNode.isEmpty) {
        Response.json("You must provide a valid end node !")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(value) => {
                val finalStartNode = startNode.getOrElse("")
                val finalEndNode = endNode.getOrElse("")
                if (value.hasVertex(finalStartNode, finalEndNode)) {
                    Response.json("The edge already exists in the graph !")
                } else {
                    val finalGraph = value.addEdge(finalStartNode, finalEndNode, weight.getOrElse(0))
                    GraphSerialization.writeToFile(finalGraph, finalGraphName)
                    Response.json("The graph has been succesfully updated !")
                }
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def removeEdgeController(req: Request): Response = {
    val graphName = req.queryParam("name")
    val startNode = req.queryParam("startNode")
    val endNode = req.queryParam("endNode")
    if (graphName.isEmpty) {
        Response.json("You must provide a graph name")
    } else if (startNode.isEmpty) {
        Response.json("You must provide a valid start node !")
    } else if (endNode.isEmpty) {
        Response.json("You must provide a valid end node !")
    } else {
        val finalGraphName = graphName.getOrElse("")
        GraphSerialization.readFromFile[String](finalGraphName) match {
            case Right(value) => {
                val finalStartNode = startNode.getOrElse("")
                val finalEndNode = endNode.getOrElse("")
                if (!value.hasVertex(finalStartNode, finalEndNode)) {
                    Response.json("The edge doesn't exist in the graph !")
                } else {
                    val finalGraph = value.removeEdge(finalStartNode, finalEndNode)
                    GraphSerialization.writeToFile(finalGraph, finalGraphName)
                    Response.json("The graph has been succesfully updated !")
                }
            }
            case Left(value) => Response.json("The graph has not been found")
        }
    }
}

def createGraphController: Handler[Any, Response, Request, Response] =
    Handler.fromFunctionZIO { req =>
        req.body.asString(Charsets.Utf8)
            .catchAll(_ => ZIO.succeed("Error reading request body"))
            .map(fromJSON[String])
            .map {
                case Left(_) => Response.json("Something went wrong!")
                case Right(value) =>
                    val graphName = value.graphInformations.name
                    if (graphName.isEmpty) {
                        Response.json("A name is required")
                    } else {
                        GraphSerialization.writeToFile(value, graphName)
                        Response.json("Graph successfully created")
                    }
            }
    }