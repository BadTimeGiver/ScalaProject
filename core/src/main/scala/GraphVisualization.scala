package fr.scalaproject.core

import java.io._

object GraphVisualization {
    def createDotFile(path: String, dotRepresentation: String) = {
        val dotFile = new PrintWriter(new File(path))
        dotFile.write(dotRepresentation)
        dotFile.close()
    }

    def generateGraph(graph: Graph, name: String): Boolean = {
        val directoryPath = s"output/${name}"
        val dotGraphPath = s"${directoryPath}/Graph.dot"
        val pngFilePath = s"${directoryPath}/Graph.png"
        if(createDirectory(directoryPath)) {
            createDotFile(dotGraphPath, graphToDOT(graph))
            val process = new ProcessBuilder("dot", "-Tpng", dotGraphPath, "-o", pngFilePath).redirectErrorStream(true).start()
            val exitCode = process.waitFor()
            exitCode == 0
        } else {
            false
        }
    }

    def createDirectory(directoryName: String): Boolean = {
        val dir = new  File(directoryName)
        if (!dir.exists()) {
            dir.mkdirs()
        } else {
            false
        }
    }

    def graphToDOT(graph: Graph): String = {
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