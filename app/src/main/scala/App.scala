package fr.scalaproject.app

import fr.scalaproject.core._
import zio._
import zio.http._
import fr.scalaproject.core.GraphSerialization.fromJSON
import fr.scalaproject.core._
import fr.scalaproject.core.GraphSerialization.writeToFile
import fr.scalaproject.core.GraphOperations._
import fr.scalaproject.core.GraphVisualization.graphToDOT
import java.nio.charset.Charset

object Application extends ZIOAppDefault {
    val routes: Routes[Any, Response] = Routes(
        // Graph Interfaces Endpoints
        Method.POST / "graph" -> handler { createGraphController },
        Method.GET / "display-graph" -> handler { displayGraphController },
        Method.GET / "add-vertex" -> handler { addVertexController },
        Method.DELETE / "remove-vertex" -> handler { removeVertexController },
        Method.GET / "add-edge" -> handler { addEdgeController },
        Method.DELETE / "remove-edge" -> handler { removeEdgeController },

        // Graph Operations Endpoints
        Method.GET / "bfs" -> handler { bfsController },
        Method.GET / "dfs" -> handler {  dfsController },
        Method.GET / "has_cycle" -> handler { hasCycleController },
        Method.GET / "topological-sort" -> handler { topologicalSortController },
        Method.GET / "dijkstra" -> handler { dijkstraController },
        Method.GET / "floyd-warshall" -> handler { floydWarshallController },
    )

    override val run = Server.serve(routes).provide(Server.defaultWithPort(8084))
}