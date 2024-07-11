// import scala.collection.mutable

// object Main {
//     val graph = Graph(
//         graphInformations = GraphInformations(
//             name = "TestGraph",
//             isWeighted = false,
//             isBidirectional = false
//         ),
//         nodes = List(
//             Node(id = 1, edges = List(Edge(to = 2, weight = 3))),
//             Node(id = 2, edges = List(Edge(to = 3, weight = 5))),
//             Node(id = 3, edges = List(Edge(to = 1, weight = 2))),
//         )
//     )

//     val notCycleGraph = Graph(
//         graphInformations = GraphInformations(
//             name = "TestGraph",
//             isWeighted = false,
//             isBidirectional = false
//         ),
//         nodes = List(
//             Node(id = 1, edges = List(Edge(to = 2, weight = 3))),
//             Node(id = 2, edges = List(Edge(to = 3, weight = 5))),
//             Node(id = 3, edges = List()),
//         )
//     )

//     def main(args: Array[String]): Unit = {
//         // println("\n\n--------------------------------\n\n")
//         // println(s"BFS Order starting from node 1: ${GraphOperations.bfs(graph, 1).mkString(", ")}")
//         // println(s"BFS Order starting from node 2: ${GraphOperations.bfs(graph, 2).mkString(", ")}")
//         // println(s"BFS Order starting from node 3: ${GraphOperations.bfs(graph, 3).mkString(", ")}")
//         // println(s"BFS Order starting from node 4: ${GraphOperations.bfs(graph, 4).mkString(", ")}")
//         // println("\n\n--------------------------------\n\n")

//         println("\n\n--------------------------------\n\n")
//         println(s"Le graphe contient un cycle: ${GraphOperations.hasCycle(graph)}")
//         println(s"Le graphe ne contient pas de cycle: ${GraphOperations.hasCycle(notCycleGraph)}")
//         println("\n\n--------------------------------\n\n")

//         // println("\n\n--------------------------------\n\n")
//         // println(s"Topological order: ${GraphOperations.topologicalSort(graph).mkString(", ")}")
//         // println("\n\n--------------------------------\n\n")

//         // println("\n\n--------------------------------\n\n")
//         // println(s"Les distances les plus courtes à partir du nœud 1 :")
//         // val distances = GraphOperations.dijkstra(graph, 1)
//         // distances.foreach { case (nodeId, distance) => println(s"Nœud $nodeId : $distance") }
//         // println("\n\n--------------------------------\n\n")

//         // println(s"Les distances les plus courtes à partir du nœud 1 :")
//         // val distances = GraphOperations.floyd(graph)
//         // distances.foreach { case (nodeId, distance) => println(s"Nœud $nodeId : $distance") }

//         // println("\n\n--------------------------------\n\n")
//         // println(s"BFS Order starting from node 1: ${GraphOperations.dfs(graph, 1).mkString(", ")}")
//         // println(s"BFS Order starting from node 2: ${GraphOperations.dfs(graph, 2).mkString(", ")}")
//         // println(s"BFS Order starting from node 3: ${GraphOperations.dfs(graph, 3).mkString(", ")}")
//         // println(s"BFS Order starting from node 4: ${GraphOperations.dfs(graph, 4).mkString(", ")}")
//         // println("\n\n--------------------------------\n\n")
//     }
// }

object Main {
    val graph = Graph(
        graphInformations = GraphInformations(
            name = "TestGraph",
            isWeighted = false,
            isBidirectional = false
        ),
        nodes = List(
            Node(id = 1, edges = List(Edge(to = 2, weight = 3))),
            Node(id = 2, edges = List(Edge(to = 3, weight = 5))),
            Node(id = 3, edges = List(Edge(to = 1, weight = 2))),
            Node(id = 4, edges = List(Edge(to = 1, weight = 2))),
            Node(id = 5, edges = List(Edge(to = 1, weight = 2))),
            Node(id = 6, edges = List(Edge(to = 1, weight = 2))),
        )
    )

    def main(args: Array[String]): Unit = {
        GraphVisualization.generateGraph(graph, "Test2")
    }
}