case class Edge(to: Int, weight: Int)
case class Node(id: Int, edges: List[Edge])
case class GraphInformations(name: String, isWeighted: Boolean, isBidirectional: Boolean)
case class Graph(graphInformations: GraphInformations, nodes: List[Node])

