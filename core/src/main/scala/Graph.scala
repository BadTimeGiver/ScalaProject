package fr.scalaproject.core

import java.io._

case class Edge(to: Int, weight: Int)
case class Node(id: Int, edges: List[Edge])
case class GraphInformations(name: String, isWeighted: Boolean, isBidirectional: Boolean)

case class Graph(graphInformations: GraphInformations, nodes: List[Node]) {
    private val nodeMap: Map[Int, Node] = nodes.map(node => node.id -> node).toMap

    def addVertex(id: Int): Graph = {
        if (!nodeMap.contains(id)) {
            val newNode = Node(id, List())
            Graph(graphInformations, nodes :+ newNode)
        } else {
            this
        }
    }

    def addEdge(from: Int, to: Int, weight: Int = 1): Graph = {
        if (nodeMap.contains(from) && nodeMap.contains(to)) {
            val fromNode = nodeMap(from)
            val isAlreadyAdded = fromNode.edges.exists({ edge => edge.to == to })
            if(isAlreadyAdded) {
                this
            } else {
                val updatedFromNode = fromNode.copy(edges = Edge(to, weight) :: fromNode.edges)
                val updatedNodeMap = nodeMap.updated(from, updatedFromNode)
                val finalNodeMap =
                if (graphInformations.isBidirectional) {
                    val toNode = updatedNodeMap(to)
                    val updatedToNode = toNode.copy(edges = Edge(from, weight) :: toNode.edges)
                    updatedNodeMap.updated(to, updatedToNode)
                } else {
                    updatedNodeMap
                }

                Graph(graphInformations, finalNodeMap.values.toList)
            }
        } else {
            this
        }
    }

    def removeVertex(id: Int): Graph = {
        val updatedNodeMap = nodeMap - id
        val finalNodeMap = updatedNodeMap.map {
            case (nodeId, node) => nodeId -> node.copy(edges = node.edges.filterNot(_.to == id))
        }
        Graph(graphInformations, finalNodeMap.values.toList)
    }

    def removeEdge(from: Int, to: Int): Graph = {
        if (nodeMap.contains(from)) {
            val fromNode = nodeMap(from)
            val updatedFromNode = fromNode.copy(edges = fromNode.edges.filterNot(_.to == to))
            val updatedNodeMap = nodeMap.updated(from, updatedFromNode)
            val finalNodeMap = if (graphInformations.isBidirectional && nodeMap.contains(to)) {
                val toNode = updatedNodeMap(to)
                val updatedToNode = toNode.copy(edges = toNode.edges.filterNot(_.to == from))
                updatedNodeMap.updated(to, updatedToNode)
            } else {
                updatedNodeMap
            }
            Graph(graphInformations, finalNodeMap.values.toList)
        } else {
            this
        }
    }
}

