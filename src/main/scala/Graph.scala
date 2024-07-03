import scala.collection.mutable
case class Edge(to: Int, weight: Int)
case class Node(id: Int, edges: List[Edge])
case class GraphInformations(name: String, isWeighted: Boolean, isBidirectional: Boolean)
case class Graph(graphInformations: GraphInformations, nodes: List[Node]) {
    private val nodeMap: mutable.Map[Int, Node] = mutable.Map(nodes.map(node => node.id -> node): _*)
    def addVertex(id: Int): Graph = {
        if (!nodeMap.contains(id)) {
            nodeMap += (id -> Node(id, List()))
        }
        copy(nodes = nodeMap.values.toList)
    }
}

