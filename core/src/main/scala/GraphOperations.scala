package fr.scalaproject.core

import scala.collection.immutable._
import scala.annotation.tailrec

object GraphOperations {
    def bfs[T](graph: Graph[T], startId: T): List[T] = {
        @tailrec
        def bfsRecursive(nodeMap: Map[T, Node[T]], queue: List[T], visited: Set[T], bfsOrder: List[T]): List[T] = {
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
        if (!nodeMap.contains(startId)) {
            List()
        } else {
            bfsRecursive(nodeMap, List(startId), Set(startId), List())
        }
    }

    def dfs[T](graph: Graph[T], startId: T): List[T] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap

        @tailrec
        def dfsRecursive(stack: List[T], visited: Set[T], dfsOrder: List[T]): List[T] = {
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

        if (!nodeMap.contains(startId)) {
            List()
        } else {
            dfsRecursive(List(startId), Set.empty, List.empty)
        }
    }

    def dijkstra[T](graph: Graph[T], startId: T)(implicit ord: Ordering[T]): Map[T, Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val initialDistances: Map[T, Int] = Map().withDefaultValue(Int.MaxValue).updated(startId, 0)
        val initialPriorityQueue: SortedSet[(T, Int)] = SortedSet((startId, 0))(Ordering.by(-_._2))

        @tailrec
        def loop(distances: Map[T, Int], priorityQueue: SortedSet[(T, Int)]): Map[T, Int] = {
            if (priorityQueue.isEmpty) {
                distances
            } else {
                val ((currentId, currentDist), restQueue) = (priorityQueue.head, priorityQueue.tail)
                val currentNode = nodeMap(currentId)

                if (currentDist <= distances(currentId)) {
                    val (newDistances, newQueue) = currentNode.edges.foldLeft((distances, restQueue)) {
                        case ((distAcc, queueAcc), Edge(neighborId, Some(weight))) =>
                            val distance = currentDist + weight
                            if (distance < distAcc(neighborId)) {
                                (distAcc.updated(neighborId, distance), queueAcc + ((neighborId, distance)))
                            } else {
                                (distAcc, queueAcc)
                            }
                        case ((distAcc, queueAcc), _) =>
                            (distAcc, queueAcc)
                    }
                    loop(newDistances, newQueue)
                } else {
                    loop(distances, restQueue)
                }
            }
        }

        loop(initialDistances, initialPriorityQueue)
    }

    def floyd[T](graph: Graph[T])(implicit ord: Ordering[T]): Map[(T, T), Int] = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap
        val nodeIds = nodeMap.keys.toList
        val initialDistances = (for {
            id <- nodeIds
            edge <- nodeMap(id).edges
            weight <- edge.weight
        } yield ((id, edge.to) -> weight)).toMap.withDefaultValue(Int.MaxValue)

        val distances = nodeIds.foldLeft(initialDistances) { (distances, id) =>
            distances.updated((id, id), 0)
        }

        def updateDistances(d: Map[(T, T), Int], i: T, j: T, k: T): Map[(T, T), Int] = {
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

    def topologicalSort[T](graph: Graph[T])(implicit ord: Ordering[T]): List[T] = {
        if (hasCycle(graph)) {
            List()
        } else {
            val nodeMap = graph.nodes.map(node => node.id -> node).toMap
            val initialVisited = Set[T]()
            val initialStack = List[T]()

            def visit(node: Node[T], visited: Set[T], stack: List[T]): (Set[T], List[T]) = {
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

    def hasCycle[T](graph: Graph[T])(implicit ord: Ordering[T]): Boolean = {
        val nodeMap = graph.nodes.map(node => node.id -> node).toMap

        def visit(nodeId: T, visited: Set[T], recStack: Set[T]): (Boolean, Set[T], Set[T]) = {
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
