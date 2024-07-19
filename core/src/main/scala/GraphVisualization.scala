package fr.scalaproject.core

import java.io._

object GraphVisualization {
    implicit class GraphToDot[T](graph: Graph[T]) {
        def toDOT: String = {
            val sb = new StringBuilder
            sb.append("digraph G {\n")

            graph.nodes.foreach { node =>
                sb.append(s"  ${node.id};\n")
                node.edges.foreach { edge =>
                    sb.append(s"  ${node.id} -> ${edge.to} [label=${edge.weight}];\n")
                }
            }

            sb.append("}")
            sb.toString()
        }
    }
}
