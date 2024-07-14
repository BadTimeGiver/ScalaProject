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