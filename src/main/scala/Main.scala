import scala.collection.mutable

object GraphExample {
    case class Edge(to: Int)
    case class Node(id: Int, edges: List[Edge])
    case class GraphInformations(name: String, isWeighted: Boolean, isBidirectional: Boolean)
    case class Graph(graphInformations: GraphInformations, nodes: List[Node])

    val graph = Graph(
        graphInformations = GraphInformations(
            name = "TestGraph",
            isWeighted = false,
            isBidirectional = false
        ),
        nodes = List(
            Node(id = 1, edges = List(Edge(to = 2), Edge(to = 3), Edge(to = 4))),
            Node(id = 2, edges = List(Edge(to = 1))),
            Node(id = 3, edges = List(Edge(to = 3), Edge(to = 2))),
            Node(id = 4, edges = List(Edge(to = 1), Edge(to = 2)))
        )
    )

    def bfs(graph: Graph, startId: Int): List[Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val visited = scala.collection.mutable.Set[Int]()
        val queue = scala.collection.mutable.Queue[Int]()
        val bfsOrder = scala.collection.mutable.ListBuffer[Int]()

        queue.enqueue(startId)
        visited.add(startId)

        while (queue.nonEmpty) {
            val currentId = queue.dequeue()
            bfsOrder += currentId

            val currentNode = nodeMap(currentId)
            for (edge <- currentNode.edges if !visited.contains(edge.to)) {
                queue.enqueue(edge.to)
                visited.add(edge.to)
            }
        }

        bfsOrder.toList
    }

    def main(args: Array[String]): Unit = {
        println("\n\n--------------------------------\n\n")
        println(s"BFS Order starting from node 1: ${bfs(graph, 1).mkString(", ")}")
        println(s"BFS Order starting from node 2: ${bfs(graph, 2).mkString(", ")}")
        println(s"BFS Order starting from node 3: ${bfs(graph, 3).mkString(", ")}")
        println(s"BFS Order starting from node 4: ${bfs(graph, 4).mkString(", ")}")
        println("\n\n--------------------------------\n\n")
    }
}