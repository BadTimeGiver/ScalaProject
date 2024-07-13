package fr.scalaproject.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GraphOperationsSpec extends AnyFlatSpec with Matchers {
    "bfs" should "return the correct BFS order" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0), Edge(3, 0))),
                Node(2, List(Edge(4, 0))),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.bfs(graph, 1)
        result shouldBe List(1, 2, 3, 4)
    }

    it should "not visit nodes in disconnected components" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List()),
                Node(4, List(Edge(5, 0))),
                Node(5, List())
            )
        )

        val result = GraphOperations.bfs(graph, 1)
        result shouldBe List(1, 2, 3)
    }

    // Graphe cyclique
    it should "handle cycles correctly and not visit nodes multiple times" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0), Edge(3, 0))),
                Node(2, List(Edge(4, 0))),
                Node(3, List(Edge(1, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.bfs(graph, 1)
        result shouldBe List(1, 2, 3, 4)
    }

    // Noeud absent
    "bfs" should "return an empty list if the start node is not in the graph" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List())
            )
        )

        val result = GraphOperations.bfs(graph, 4)
        result shouldBe List()
    }

    "dijkstra" should "return the shortest paths from the start node" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = true, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 1), Edge(3, 4))),
                Node(2, List(Edge(3, 2), Edge(4, 5))),
                Node(3, List(Edge(4, 1))),
                Node(4, List())
            )
        )

        val result = GraphOperations.dijkstra(graph, 1)
        result shouldBe Map(1 -> 0, 2 -> 1, 3 -> 3, 4 -> 4)
    }

    "topologicalSort" should "return the correct topological order for a DAG" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.topologicalSort(graph)
        result shouldBe List(1, 2, 3, 4)
    }

    it should "return an empty list for a graph with a cycle" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List(Edge(1, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.topologicalSort(graph)
        result shouldBe List()
    }

    "hasCycle" should "detect a cycle in the graph" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List(Edge(1, 0)))
            )
        )

        val result = GraphOperations.hasCycle(graph)
        result shouldBe true
    }

    it should "not detect a cycle in a DAG" in {
        val graph = Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.hasCycle(graph)
        result shouldBe false
    }
}
