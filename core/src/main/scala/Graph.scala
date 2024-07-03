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
            val updatedEdges = Edge(to, weight) :: fromNode.edges
            nodeMap.update(from, fromNode.copy(edges = updatedEdges))

            if (graphInformations.isBidirectional) {
                val toNode = nodeMap(to)
                val updatedEdgesTo = Edge(from, weight) :: toNode.edges
                nodeMap.update(to, toNode.copy(edges = updatedEdgesTo))
            }
        }
        copy(nodes = nodeMap.values.toList)
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

