package fr.scalaproject.core

import scala.collection.immutable.*
import scala.annotation.tailrec

object GraphOperations {
    def bfs(graph: Graph, startId: Int): List[Int] = {
        @tailrec
        def bfsRecursive( nodeMap: Map[Int, Node], queue: List[Int], visited: Set[Int], bfsOrder: List[Int]): List[Int] = {
            queue match {
                case Nil => bfsOrder
                case currentId :: rest =>
                    val currentNode = nodeMap(currentId)
                    val newEdges = currentNode.edges.filterNot(edge => visited.contains(edge.to))
                    val newQueue = rest ++ newEdges.map(_.to)
                    val newVisited = visited ++ newEdges.map(_.to)
                    bfsRecursive(nodeMap, newQueue, newVisited, bfsOrder :+ currentId)
            }
        }

        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        bfsRecursive(nodeMap, List(startId), Set(startId), List())
    }


    def dfs(graph: Graph, startId: Int): List[Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap

        @tailrec
        def dfsRecursive(stack: List[Int],visited: Set[Int],dfsOrder: List[Int]): List[Int] = {
            stack match {
                case Nil => dfsOrder
                case currentId :: rest =>
                    if (visited.contains(currentId)) {
                        dfsRecursive(rest, visited, dfsOrder)
                    } else {
                        val currentNode = nodeMap(currentId)
                        val newStack = currentNode.edges.map(_.to).filterNot(visited.contains) ++ rest
                        dfsRecursive(newStack, visited + currentId, dfsOrder :+ currentId)
                    }
            }
        }

        dfsRecursive(List(startId), Set.empty, List.empty)
    }

    def dijkstra(graph: Graph, startId: Int): Map[Int, Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val initialDistances: Map[Int, Int] = ListMap().withDefaultValue(Int.MaxValue).updated(startId, 0)
        val initialPriorityQueue: SortedSet[(Int, Int)] = SortedSet((startId, 0))(Ordering.by(-_._2))

        @tailrec
        def loop(distances: Map[Int, Int], priorityQueue: SortedSet[(Int, Int)]): Map[Int, Int] = {
            if (priorityQueue.isEmpty) {
                distances
            } else {
                val ((currentId, currentDist), restQueue) = (priorityQueue.head, priorityQueue.tail)
                val currentNode = nodeMap(currentId)

                if (currentDist <= distances(currentId)) {
                    val (newDistances, newQueue) = currentNode.edges.foldLeft((distances, restQueue)) {
                        case ((distAcc, queueAcc), Edge(neighborId, weight)) =>
                            val distance = currentDist + weight
                            if (distance < distAcc(neighborId)) {
                                (distAcc.updated(neighborId, distance), queueAcc + ((neighborId, distance)))
                            } else {
                                (distAcc, queueAcc)
                            }
                    }
                    loop(newDistances, newQueue)
                } else {
                    loop(distances, restQueue)
                }
            }
        }

        loop(initialDistances, initialPriorityQueue)
    }

    def floyd(graph: Graph): Map[(Int, Int), Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val nodeIds = nodeMap.keys.toList
        val initialDistances = (for {
            id <- nodeIds
            edge <- nodeMap(id).edges
        } yield ((id, edge.to) -> edge.weight)).toMap.withDefaultValue(Int.MaxValue)

        val distances = nodeIds.foldLeft(initialDistances) { (distances, id) =>
            distances.updated((id, id), 0)
        }

        def updateDistances(d: Map[(Int, Int), Int], i: Int, j: Int, k: Int): Map[(Int, Int), Int] = {
            val distIK = d((i, k))
            val distKJ = d((k, j))
            if (distIK != Int.MaxValue && distKJ != Int.MaxValue) {
                val newDist = distIK + distKJ
                val currentDist = d((i, j))
                if (newDist < currentDist) {
                    d.updated((i, j), newDist)
                } else {
                    d
                }
            } else {
                d
            }
        }

        val finalDistances = nodeIds.foldLeft(distances) { (d, k) =>
            nodeIds.foldLeft(d) { (d1, i) =>
                nodeIds.foldLeft(d1) { (d2, j) =>
                    updateDistances(d2, i, j, k)
                }
            }
        }

        finalDistances
    }



    def topologicalSort(graph: Graph): List[Int] = {
        if (hasCycle(graph)) {
            List()
        } else {
            val nodeMap = graph.nodes.map(node => node.id -> node).toMap
            val initialVisited = Set[Int]()
            val initialStack = List[Int]()

            def visit(node: Node, visited: Set[Int], stack: List[Int]): (Set[Int], List[Int]) = {
                if (!visited.contains(node.id)) {
                    val newVisited = visited + node.id
                    val (finalVisited, updatedStack) = node.edges.foldLeft((newVisited, stack)) {
                        case ((v, s), edge) => visit(nodeMap(edge.to), v, s)
                    }
                    (finalVisited, node.id :: updatedStack)
                } else {
                    (visited, stack)
                }
            }

            val (finalVisited, sortedStack) = graph.nodes.foldLeft((initialVisited, initialStack)) {
                case ((v, s), node) => if (!v.contains(node.id)) visit(node, v, s) else (v, s)
            }
            sortedStack
        }
    }

    def hasCycle(graph: Graph): Boolean = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap

        def visit(nodeId: Int, visited: Set[Int], recStack: Set[Int]): (Boolean, Set[Int], Set[Int]) = {
            if (!visited.contains(nodeId)) {
                val newVisited = visited + nodeId
                val newRecStack = recStack + nodeId
                val neighbors = nodeMap(nodeId).edges.map(_.to)

                neighbors.foldLeft((false, newVisited, newRecStack)) {
                    case ((true, updatedVisited, updatedRecStack), _) => (true, updatedVisited, updatedRecStack)
                    case ((false, newVisited, newRecStack), neighbor) if !newVisited.contains(neighbor) =>
                        val (hasCycle, updatedVisited, updatedRecStack) = visit(neighbor, newVisited, newRecStack)
                        if (hasCycle) {
                            (true, updatedVisited, updatedRecStack)
                        } else {
                            (false, newVisited, newRecStack - nodeId)
                        }
                    case ((false, newVisited, newRecStack), neighbor) if newRecStack.contains(neighbor) => (true, newVisited, newRecStack)
                    case (result, _) => result
                }
            } else {
                (false, visited, recStack)
            }
        }

        graph.nodes.exists { node =>
            val (hasCycle, _, _) = visit(node.id, Set.empty, Set.empty)
            hasCycle
        }
    }
}