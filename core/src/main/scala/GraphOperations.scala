import scala.collection.mutable

object GraphOperations {
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

    def dfs(graph: Graph, startId: Int): List[Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val visited = scala.collection.mutable.Set[Int]()
        val stack = scala.collection.mutable.Stack[Int]()
        val dfsOrder = scala.collection.mutable.ListBuffer[Int]()

        stack.push(startId)

        while (stack.nonEmpty) {
            val currentId = stack.pop()

            if (!visited.contains(currentId)) {
                dfsOrder += currentId
                visited.add(currentId)

                val currentNode = nodeMap(currentId)
                for (edge <- currentNode.edges) {
                    if (!visited.contains(edge.to)) {
                        stack.push(edge.to)
                    }
                }
            }
            print(stack.mkString(", "))
        }

        dfsOrder.toList
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

    def floyd(graph: Graph): Map[(Int, Int), Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val nodeIds = nodeMap.keys
        val distances = mutable.Map[(Int, Int), Int]().withDefaultValue(Int.MaxValue)
        for ((id, node) <- nodeMap) {
            distances((id, id)) = Int.MaxValue
            for (edge <- node.edges) {
                distances((id, edge.to)) = edge.weight
            }
        }
        for (k <- nodeIds) {
            for (i <- nodeIds) {
                for (j <- nodeIds) {
                    if (distances((i, k)) != Int.MaxValue && distances((k, j)) != Int.MaxValue && i!=j) {
                        distances((i, j)) = Math.min(distances((i, j)), distances((i, k)) + distances((k, j)))
                    }
                }
            }
        }

        distances.toMap
    }


    def topologicalSort(graph: Graph): List[Int] = {
        if(hasCycle(graph)) {
            List()
        } else {
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
}