import scala.collection.mutable

object GraphExample {
    case class Edge(to: Int, weight: Int)
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
            Node(id = 1, edges = List(Edge(to = 2, weight = 3), Edge(to = 3, weight = 1), Edge(to = 4, weight = 4))),
            Node(id = 2, edges = List(Edge(to = 1, weight = 5))),
            Node(id = 3, edges = List(Edge(to = 3, weight = 2), Edge(to = 2, weight = 6))),
            Node(id = 4, edges = List(Edge(to = 1, weight = 2), Edge(to = 2, weight = 3))),
            Node(id = 5, edges = List(Edge(to = 1, weight = 4), Edge(to = 2, weight = 1)))
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

    def dijkstra(graph: Graph, startId: Int): Map[Int, Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val distances = mutable.Map[Int, Int]().withDefaultValue(Int.MaxValue)
        val priorityQueue = mutable.PriorityQueue[(Int, Int)]()(Ordering.by(-_._2))

        distances(startId) = 0
        priorityQueue.enqueue((startId, 0))

        while (priorityQueue.nonEmpty) {
            val (currentId, currentDist) = priorityQueue.dequeue()
            val currentNode = nodeMap(currentId)

            if (currentDist <= distances(currentId)) {
                for (Edge(neighborId, weight) <- currentNode.edges) {
                    val distance = currentDist + weight
                    if (distance < distances(neighborId)) {
                        distances(neighborId) = distance
                        priorityQueue.enqueue((neighborId, distance))
                    }
                }
            }
        }
        distances.toMap
    }

    def topologicalSort(graph: Graph): List[Int] = {
        if(hasCycle(graph)) {
            return List()
        }

        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val visited = mutable.Set[Int]()
        val stack = mutable.Stack[Int]()

        def visit(node: Node): Unit = {
            if (!visited.contains(node.id)) {
                visited.add(node.id)
                node.edges.foreach(edge => visit(nodeMap(edge.to)))
                stack.push(node.id)
            }
        }

        graph.nodes.foreach(node => if (!visited.contains(node.id)) visit(node))
        stack.toList
    }

    def hasCycle(graph: Graph): Boolean = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val visited = mutable.Set[Int]()
        val recStack = mutable.Set[Int]()

        def visit(nodeId: Int): Boolean = {
            if (!visited.contains(nodeId)) {
                visited.add(nodeId)
                recStack.add(nodeId)

                val neighbors = nodeMap(nodeId).edges.map(_.to)
                for (neighbor <- neighbors) {
                    if (!visited.contains(neighbor) && visit(neighbor)) {
                        return true
                    } else if (recStack.contains(neighbor)) {
                        return true
                    }
                }
            }

            recStack.remove(nodeId)
            false
        }

        graph.nodes.exists(node => visit(node.id))
    }


    def main(args: Array[String]): Unit = {
        println("\n\n--------------------------------\n\n")
        println(s"BFS Order starting from node 1: ${bfs(graph, 1).mkString(", ")}")
        println(s"BFS Order starting from node 2: ${bfs(graph, 2).mkString(", ")}")
        println(s"BFS Order starting from node 3: ${bfs(graph, 3).mkString(", ")}")
        println(s"BFS Order starting from node 4: ${bfs(graph, 4).mkString(", ")}")
        println("\n\n--------------------------------\n\n")

        println("\n\n--------------------------------\n\n")
        println(s"Le graphe contient un cycle: ${hasCycle(graph)}")
        println("\n\n--------------------------------\n\n")

        println("\n\n--------------------------------\n\n")
        println(s"Topological order: ${topologicalSort(graph).mkString(", ")}")
        println("\n\n--------------------------------\n\n")

        println("\n\n--------------------------------\n\n")
        println(s"Les distances les plus courtes à partir du nœud 1 :")
        val distances = dijkstra(graph, 1)
        distances.foreach { case (nodeId, distance) => println(s"Nœud $nodeId : $distance") }
    }
}