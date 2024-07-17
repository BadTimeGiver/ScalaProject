package fr.scalaproject.core

import fr.scalaproject.core._
import fr.scalaproject.core.GraphOperations._

object Main extends App {

  // Create graph information
  val graphInfo = GraphInformations(name = "TestGraph", isWeighted = true, isBidirectional = true)

  // Create initial nodes
  val node1 = Node(id = "test1", edges = List())
  val node2 = Node(id = "test2", edges = List())
  val node3 = Node(id = "test3", edges = List())
  val graph = Graph(graphInfo, nodes = List(node1, node2, node3))

  // Add edges
  val graphWithEdges = graph
    .addEdge("test1", "test2", 5)
    .addEdge("test2", "test3", 3)
    .addEdge("test3", "test1", 2)

  // Print initial graph with edges
  println(s"Initial Graph with Edges: $graphWithEdges")

  // Test BFS
  val bfsResult = GraphOperations.bfs(graphWithEdges, "test1")
  println(s"BFS starting from node 1: $bfsResult")

  // Test DFS
  val dfsResult = GraphOperations.dfs(graphWithEdges, "test1")
  println(s"DFS starting from node 1: $dfsResult")

  // Test Dijkstra's algorithm
  val dijkstraResult = GraphOperations.dijkstra(graphWithEdges, "test1")
  println(s"Dijkstra's algorithm starting from node 1: $dijkstraResult")

  // Test Floyd-Warshall algorithm
  val floydResult = GraphOperations.floyd(graphWithEdges)
  println("Floyd-Warshall algorithm result:")
  floydResult.foreach { case ((i, j), dist) =>
    println(s"Distance from $i to $j: $dist")
  }

  // Test topological sort (which should not be valid for cyclic graphs)
  val topoSortResult = GraphOperations.topologicalSort(graphWithEdges)
  println(s"Topological sort of the graph: $topoSortResult")

  // Test cycle detection
  val hasCycleResult = GraphOperations.hasCycle(graphWithEdges)
  println(s"Graph has cycle: $hasCycleResult")
}