package fr.scalaproject.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GraphOperationsSpec extends AnyFlatSpec with Matchers {

    "bfs" should "return the correct BFS order" in {
        val graph = Graph[Int](
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
        val graph = Graph[Int](
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

    it should "handle cycles correctly and not visit nodes multiple times" in {
        val graph = Graph[Int](
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

    it should "return an empty list if the start node is not in the graph" in {
        val graph = Graph[Int](
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

    "dfs" should "return the correct DFS order" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0), Edge(3, 0))),
                Node(2, List(Edge(4, 0))),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.dfs(graph, 1)
        result shouldBe List(1, 2, 4, 3)
    }

    it should "not visit nodes in disconnected components" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List()),
                Node(4, List(Edge(5, 0))),
                Node(5, List())
            )
        )

        val result = GraphOperations.dfs(graph, 1)
        result shouldBe List(1, 2, 3)
    }

    it should "handle cycles correctly and not visit nodes multiple times" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0), Edge(3, 0))),
                Node(2, List(Edge(4, 0))),
                Node(3, List(Edge(1, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.dfs(graph, 1)
        result shouldBe List(1, 2, 4, 3)
    }

    it should "return an empty list if the start node is not in the graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List())
            )
        )

        val result = GraphOperations.dfs(graph, 4)
        result shouldBe List()
    }

    "hasCycle" should "detect a cycle in the graph" in {
        val graph = Graph[Int](
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
        val graph = Graph[Int](
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

    it should "return false for a graph with disconnected components without cycles" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List()),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.hasCycle(graph)
        result shouldBe false
    }

    it should "return false for a graph without nodes" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List()
        )

        val result = GraphOperations.hasCycle(graph)
        result shouldBe false
    }

    "topologicalSort" should "return a correct topological order for a simple acyclic graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0), Edge(3, 0))),
                Node(2, List(Edge(4, 0))),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = GraphOperations.topologicalSort(graph)
        result shouldBe List(1, 3, 2, 4)
    }

    it should "return an empty List for a cyclic graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0))),
                Node(2, List(Edge(3, 0))),
                Node(3, List(Edge(1, 0)))
            )
        )

        val result = GraphOperations.topologicalSort(graph)
        result shouldBe List()
    }

    "floydWarshall" should "compute shortest paths for a simple weighted graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = true, isBidirectional = true),
            List(
                Node(1, List(Edge(2, 1), Edge(3, 4))),
                Node(2, List(Edge(3, 2), Edge(4, 5))),
                Node(3, List(Edge(4, 1))),
                Node(4, List())
            )
        )

        val result = GraphOperations.floyd(graph)
        result shouldBe Map(
            (1, 1) -> 0,
            (1, 2) -> 1,
            (1, 3) -> 3,
            (1, 4) -> 4,
            (2, 2) -> 0,
            (2, 3) -> 2,
            (2, 4) -> 3,
            (3, 3) -> 0,
            (3, 4) -> 1,
            (4, 4) -> 0
        )
    }

    it should "handle a graph with no edges correctly" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = true, isBidirectional = true),
            List(
                Node(1, List()),
                Node(2, List()),
                Node(3, List())
            )
        )

        val result = GraphOperations.floyd(graph)
        result shouldBe Map(
            (1, 1) -> 0,
            (2, 2) -> 0,
            (3, 3) -> 0
        )
    }

    "dijkstra" should "compute the shortest paths from the start node in a simple weighted graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = true, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 1), Edge(3, 4))),
                Node(2, List(Edge(3, 2), Edge(4, 5))),
                Node(3, List(Edge(4, 1))),
                Node(4, List())
            )
        )

        val result = GraphOperations.dijkstra(graph, 1)
        result shouldBe Map(
            1 -> 0,
            2 -> 1,
            3 -> 3,
            4 -> 4
        )
    }

    it should "return the shortest paths for a graph with disconnected components" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = true, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 1))),
                Node(2, List()),
                Node(3, List(Edge(4, 2))),
                Node(4, List())
            )
        )

        val result = GraphOperations.dijkstra(graph, 1)
        result shouldBe Map(1 -> 0, 2 -> 1)
    }

    it should "handle graphs with positive cycles correctly" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = true, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 1))),
                Node(2, List(Edge(3, 2))),
                Node(3, List(Edge(1, 3))),
                Node(4, List(Edge(3, 1)))
            )
        )

        val result = GraphOperations.dijkstra(graph, 4)
        result shouldBe Map(
            4 -> 0,
            3 -> 1,
            1 -> 4,
            2 -> 5
        )
    }
}
