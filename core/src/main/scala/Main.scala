package fr.scalaproject.core

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
        GraphSerialization.writeToFile(graph, "Test2")
    }
}