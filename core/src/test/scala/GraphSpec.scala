import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GraphSpec extends AnyFlatSpec with Matchers {
    /////////////////// -- ADD VERTEX TEST -- ///////////////////
    "Add Vertex" should "add vertex to graph" in {
        val graph = new Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0), Edge(3, 0))),
                Node(2, List(Edge(4, 0))),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = graph.addVertex(5)
        result.nodes.map(node => node.id) shouldBe List(1, 2, 3, 4, 5)
    }

    it should "not add vertex if already contains" in {
        val graph = new Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List(Edge(2, 0), Edge(3, 0))),
                Node(2, List(Edge(4, 0))),
                Node(3, List(Edge(4, 0))),
                Node(4, List())
            )
        )

        val result = graph.addVertex(4)
        result.nodes.map(node => node.id) shouldBe List(1, 2, 3, 4)
    }

    it should "add vertex to an empty graph" in {
        val graph = new Graph(GraphInformations("TestGraph", isWeighted = false, isBidirectional = false), List())
        val result = graph.addVertex(1)
        result.nodes.map(node => node.id) shouldBe List(1)
    }


    /////////////////// -- ADD EDGES TEST -- ///////////////////
    "Add Edge" should "add edge to a graph" in {
        val graph = new Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(
                Node(1, List()),
                Node(2, List())
            )
        )

        val result = graph.addEdge(1, 2, 1)
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, 1))
    }

    it should "not add edge if begin node doesn't exist" in {
        val graph = new Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, 2))), Node(2, List()))
        )

        val result = graph.addEdge(3, 2, 1)
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, 2))
    }

    it should "not add edge if end node doesn't exist" in {
        val graph = new Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, 2))), Node(2, List()))
        )

        val result = graph.addEdge(1, 3, 1)
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, 2))
    }

    it should "add edge to a graph if already present" in {
        val graph = new Graph(
            GraphInformations("TestGraph", isWeighted = false, isBidirectional = false),
            List(Node(1, List(Edge(2, 2))), Node(2, List()))
        )

        val result = graph.addEdge(1, 2, 20)
        val flattedEdgesList = result.nodes.flatMap { node => node.edges.map(edge => (node.id, edge.to, edge.weight)) }
        flattedEdgesList shouldBe List((1, 2, 2))
    }

    // "Remove Vertex" should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }

    // "Remove Vertex" should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }

    // "Remove Vertex" should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }

    // "Remove Vertex" should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }
    // it should "add edge to a graph" in {

    // }

    // "Remove Vertex" should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }

    // "Remove Edge" should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
    // it should "Remove a vertex from a graph" in {

    // }
}