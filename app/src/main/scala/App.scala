package fr.scalaproject.app

import zio._
import zio.http._


object Application extends ZIOAppDefault {
    val routes: Routes[Any, Response] = Routes(
        // Graph Interfaces Endpoints
        Method.POST / "graph" -> handler { (req: Request) => createGraphController },
        Method.GET / "display-graph" -> handler { (req: Request) => displayGraphController },
        Method.GET / "add-vertex" -> handler { (req: Request) => addVertexController },
        Method.DELETE / "remove-vertex" -> handler { (req: Request) => removeVertexController },
        Method.GET / "add-edge" -> handler { (req: Request) => addEdgeController },
        Method.DELETE / "remove-edge" -> handler { (req: Request) => removeEdgeController },

        // Graph Operations Endpoints
        Method.GET / "bfs" -> handler { (req: Request) => bfsController },
        Method.GET / "dfs" -> handler {  (req: Request) => dfsController },
        Method.GET / "has_cycle" -> handler { (req: Request) => hasCycleController },
        Method.GET / "topological-sort" -> handler { (req: Request) => topologicalSortController },
        Method.GET / "dijkstra" -> handler { (req: Request) => dijkstraController },
        Method.GET / "floyd-warshall" -> handler { (req: Request) => floydWarshallController },
    )

    override val run = Server.serve(routes).provide(Server.defaultWithPort(8084))
}