import scala.collection.mutable

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
        } else {
            this
        }
    }

    def removeVertex(id: Int): Graph = {
        nodeMap -= id
        for ((nodeId, node) <- nodeMap) {
            val updatedEdges = node.edges.filterNot(_.to == id)
            nodeMap.update(nodeId, node.copy(edges = updatedEdges))
        }
        copy(nodes = nodeMap.values.toList)
    }

    def removeEdge(from: Int, to: Int): Graph = {
        if (nodeMap.contains(from)) {
            val fromNode = nodeMap(from)
            val updatedEdges = fromNode.edges.filterNot(_.to == to)
            nodeMap.update(from, fromNode.copy(edges = updatedEdges))

            if (graphInformations.isBidirectional && nodeMap.contains(to)) {
                val toNode = nodeMap(to)
                val updatedEdgesTo = toNode.edges.filterNot(_.to == from)
                nodeMap.update(to, toNode.copy(edges = updatedEdgesTo))
            }
        }
        copy(nodes = nodeMap.values.toList)
    }
}

