# Scala M1 Project

For this project, Mehdi AMMOR, Esteban VINCENT and Zyad ZEKRI have created an API for allowing you to play with Graph and Graph Algorithm like BFS, DFS, Dijkstra, ...

## Prerequisites

List of software and versions needed to run the project.
- Scala 3.3.3
- SBT 1.x

## Installation

Instructions to set up the project locally.

```bash
git clone https://github.com/yourusername/yourproject.git

cd yourproject
```

## Execution of the project

After that, you'll need to run the `sbt` command to enter in the project.
Then, run
```bash
project app # Allow you to select the good project
run # Allow you to run the Web Server
```

## Testing the core (graph interfaces & graph operations)

For running the unit test, you will need to do the following :
1. Enter in the project with the `sbt` command
2. Select the good project with the `project core` command
3. Run the unit test with the `test` command

## Testing the app

For that, you have a [Postman Collection file](./PostmanCollection.json) that can be used with Postman, and contains all the endpoint of our API.

## Informations about the project

### Test coverage

For the Core subproject, we have made **33 unit tests**, and more precisely :
__GraphSpec__ (14)
- Add Vertex : 3
- Add Edge : 4
- Remove Vertex : 3
- Remove Edge : 4

__GraphOperationsSpec__ (19)
- BFS : 4
- DFS : 4
- HasCycle : 4
- TopologicalSort : 2
- FloydWarshall : 2
- Djikstra : 3


For the App subproject, we haven't made unit tests, but we've created a [Postman Collection file](./PostmanCollection.json).
We have made, we have made **39 API tests** using Postman, and more precisely :
__GraphSpec__ (15)
- CreateGraph : 4
- DisplayGraph : 3
- Add Vertex : 1
- Add Edge : 1
- Remove Vertex : 5
- Remove Edge : 1

__GraphOperationsSpec__ (24)
- BFS : 5
- DFS : 5
- HasCycle : 3
- TopologicalSort : 3
- FloydWarshall : 3
- Djikstra : 5

### Special Case : The Graph Structure - Choice of Implementation
We chose to use the case class GraphInformations rather than inheritance for two main reasons:
- It allowed us to encapsulate graph properties and allowed us to avoid a complex class hierarchy (and avoid encountering problems linked to Liskov Subtitution).
- It was simpler to implement.


### Project Structure :
1. Sub-project Core : Our Graph library we used in the subproject App
    1. Main : The code of the Core subproject
        1. Graph.scala : Describe the general structure of the Graph and its basic operations (add vertex, remove vertex...)
        2. GraphOperations.scala : Implements the main algorithms of the Graph
        3. GraphSerialization.scala : Allow us to serialize and deserialize the Graph in JSON
        4. GraphVisualization.scala : Allows us to convert the Graph in DOT language
    2. Tests : The different unit tests of the Core subproject

2. Sub-project App : The application server
    1. Controllers : The different controllers we used in the application
        1. GraphController.scala : The controllers related the Graph Interface
        2. GraphOperationsController.scala : The controllers related the Graph Operations
    2. App.scala : The file where we launched the server