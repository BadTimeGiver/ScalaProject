package fr.scalaproject.core

import java.io._

case class Edge[T](to: T, weight: Int)
case class Node[T](id: T, edges: List[Edge[T]])
case class GraphInformations(name: String, isWeighted: Boolean, isBidirectional: Boolean)

case class Graph[T](graphInformations: GraphInformations, nodes: List[Node[T]]) {
    private val nodeMap: Map[T, Node[T]] = nodes.map(node => node.id -> node).toMap

    def hasVertex(startId: T, endId: T): Boolean = nodeMap(startId).edges.exists({ edge => edge.to == endId })
    def hasNode(id: T): Boolean = nodeMap.contains(id)

    def addVertex(id: T): Graph[T] = {
        if (!nodeMap.contains(id)) {
            val newNode = Node(id, List())
            Graph(graphInformations, nodes :+ newNode)
        } else {
            this
        }
    }

    def addEdge(from: T, to: T, weight: Int = 1): Graph[T] = {
        if (nodeMap.contains(from) && nodeMap.contains(to)) {
            val fromNode = nodeMap(from)
            val isAlreadyAdded = fromNode.edges.exists({ edge => edge.to == to })
            if (isAlreadyAdded) {
                this
            } else {
                val updatedFromNode = fromNode.copy(edges = Edge(to, weight) :: fromNode.edges)
                val updatedNodeMap = nodeMap.updated(from, updatedFromNode)
                val finalNodeMap = if (graphInformations.isBidirectional) {
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

    def removeVertex(id: T): Graph[T] = {
        val updatedNodeMap = nodeMap - id
        val finalNodeMap = updatedNodeMap.map {
            case (nodeId, node) => nodeId -> node.copy(edges = node.edges.filterNot(_.to == id))
        }
        Graph(graphInformations, finalNodeMap.values.toList)
    }

    def removeEdge(from: T, to: T): Graph[T] = {
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
