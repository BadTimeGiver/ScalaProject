import fr.scalaproject.core._

object Main extends App {
    val nodes = List(
        Node(1, List(Edge(2, 10), Edge(3, 5))),
        Node(2, List(Edge(3, 2))),
        Node(3, List(Edge(4, 1))),
        Node(4, List())
    )

    val graphInfo = GraphInformations("ExampleGraph", isWeighted = true, isBidirectional = false)
    val graph = Graph(graphInfo, nodes)

    // Import the implicit class
    import GraphVisualization._

    // Use the toDOT method
    println(graph.toDOT)
}