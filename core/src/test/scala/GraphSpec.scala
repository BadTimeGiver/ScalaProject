package fr.scalaproject.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GraphSpec extends AnyFlatSpec with Matchers {
    /////////////////// -- ADD VERTEX TEST -- ///////////////////
    "Add Vertex" should "add vertex to graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))),
                Node(2, List(Edge(4, Some(0)))),
                Node(3, List(Edge(4, Some(0)))),
                Node(4, List())
            )
        )

        val result = graph.addVertex(5)
        result.nodes.map(node => node.id) shouldBe List(1, 2, 3, 4, 5)
    }

    it should "not add vertex if already contains" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))),
                Node(2, List(Edge(4, Some(0)))),
                Node(3, List(Edge(4, Some(0)))),
                Node(4, List())
            )
        )

        val result = graph.addVertex(4)
        result.nodes.map(node => node.id) shouldBe List(1, 2, 3, 4)
    }

    it should "add vertex to an empty graph" in {
        val graph = Graph[Int](GraphInformations("TestGraph", isWeighted = false, isBidirectional = false), List())
        val result = graph.addVertex(1)
        result.nodes.map(node => node.id) shouldBe List(1)
    }

    /////////////////// -- ADD EDGES TEST -- ///////////////////
    "Add Edge" should "add edge to a graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List()),
                Node(2, List())
            )
        )

        val result = graph.addEdge(1, 2, Some(1))
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, Some(1)))
    }

    it should "not add edge if begin node doesn't exist" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(2)))), Node(2, List()))
        )

        val result = graph.addEdge(3, 2, Some(1))
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, Some(2)))
    }

    it should "not add edge if end node doesn't exist" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(2)))), Node(2, List()))
        )

        val result = graph.addEdge(1, 3, Some(1))
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, Some(2)))
    }

    it should "not add edge to a graph if already present" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(2)))), Node(2, List()))
        )

        val result = graph.addEdge(1, 2, Some(20))
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, Some(2)))
    }

    /////////////////// -- REMOVE VERTEX TEST -- ///////////////////
    "Remove Vertex" should "remove a vertex from a graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))),
                Node(2, List(Edge(3, Some(0)))),
                Node(3, List(Edge(1, Some(0)))),
            )
        )

        val result = graph.removeVertex(3)
        result shouldBe Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)))), Node(2, List()))
        )
    }

    it should "not remove a vertex if not exists / already removed" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )

        val result = graph.removeVertex(4)
        result shouldBe Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )
    }

    it should "not remove in an empty graph" in {
        val graph = Graph[Int](GraphInformations("TestGraph", isWeighted = false, isBidirectional = false), List())
        val result = graph.removeVertex(1)
        result shouldBe Graph[Int](GraphInformations("TestGraph", isWeighted = false, isBidirectional = false), List())
    }

    /////////////////// -- REMOVE EDGE TEST -- ///////////////////
    "Remove Edge" should "remove an edge in a graph" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))),
                Node(2, List(Edge(3, Some(0)))),
                Node(3, List(Edge(1, Some(0)))))
        )

        val result = graph.removeEdge(1, 2)
        result shouldBe Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )
    }

    it should "not remove an edge if begin node does not exist" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )

        val result = graph.removeEdge(4, 2)
        result shouldBe Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )
    }

    it should "not remove an edge if end node does not exist" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )

        val result = graph.removeEdge(2, 4)
        result shouldBe Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )
    }

    it should "not remove an edge if edge does not exist" in {
        val graph = Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )

        val result = graph.removeEdge(2, 1)
        result shouldBe Graph[Int](
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, Some(0)), Edge(3, Some(0)))), Node(2, List(Edge(3, Some(0)))), Node(3, List(Edge(1, Some(0)))))
        )
    }
}
