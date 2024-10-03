# ♕ BYU CS 240 Chess
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIGEAtIGcnQCIgIYHMBOmBbAKCMwGNgB7XaAVSUlwHJVZwRIA7YIgB01ygyIft2gBlRgDdGfAUJGYxk3FJBlIcwesVi0mYJmgBBMhpREAJgcwAjTA3QAhEkTyUArr2gBiW+A8YACVIbBAkYHxQSk5oInpGFjh2LmAAWgA+FRlcAC4AegSaAB1OAAoPBlxOQkgAGmh+FAB3aksGyAJMEHAASiJsxkzs9UhciqqagnqmpFbcds7uvoHpUcz9QxMzZCRc7EhgIqnICcYT-s2jU3MkDadc6AAzEE5LaGBEaErz2qIr7a3NLDNYaXKcSifV7YVaqdYZAE3Xa5Mi4SAGSA3TzcM7VWoNWbzSyXGyA3b3R6YSzvH40V5PSiwtQaDakpEoFFojEAa0gAE9cRd-mydigKdAqe9MB5Pql1AYQDFhVt2XcQXCwdLZdx5dFOEz1uqcrktYgdWQFUrBrhgRkikk2BxuLkAEwABjdZVNcotev6XEsJHcXl8eEgXGgABlKGF9fbWCluEbGAUGChFbFSoL8Y0HHM2v1rUbRuNaScCXmiYXQZBWSrRXsDkcqtnphWWgXldcGxSGFAKN9JrVnrhKARB4wJW9cx2FgaWQiRbdOejgDBvebLfrET2EQ8+5AB2Xh818hKZfAqLzt0vycXNRefVv57Xk3kN0Jn0XMvHkk7gK6Hpeo+m5+kQAZBqOIY+Gi7zRtgng8L+jqpG+qa7Bm0BZh+uoZtWqhDFkNa5OAMaIcB2qfmBIwLjuy45CATwCjhvp4V2ZJinuuQHgOLFfjWdbdsuliQFAa6mhRZpUWxdHklxaIEJQMjnpRuFKrJYr3mMIliZoNGvkRBF5KRCEykyQw-lUDqJgB7puuBbyQZ43g+Hyomkc0UbhMA0AAOK1Eg8RWQm-5ofk2DDthIHSTE+E5FpJHeRF0ySU+1ECYu9b0YwjHMdFamcCSWVyWg+6iYePl8XqL6CRxjaHJg4DgMlyCpaBMm3pxpXceVA4texqq2vp+zDuwEQ1WhLXQGNPDfnawV-qkgFutAZRTTN-qOW4UEudQSgHHAXJrn5fzITZYVTaUVUZmttRCkWhnMmMqKrqEtRtTFnANC190ZRpewMUxH0FUVQklWV-aVflrFWn9nX1cALW3dMQr-b2vU+f1aMJRCUKcDC+m1aqK4Yv50wgJYyNcLUoN1RSrxVJjtQUwNPYJVNLOE5Nw6c9IFnzYkIVLXZq0c8SDmBhB23Ob4tijs0sQAFKUK8J3TEFguLUmj0puFkU1NDW5lOJBBkJQpG4N9zPiw9w0AFYq-j73XTEDQm2bFtW+TNtw8VHKA3lqkw4VrNAlxPFQ0Hz41ZlYMck2SMu190AtRTtODeHGMpzzgYx-9uQO9CzuG3qbvoqb5vUF7kBp6H4M9ZDKlSQVZeEB7VfQCz2OPSWuPwNCE063kfcD3NZ3-stEtOdBzT98dbDojQxi8LwGvMEL2vWgUOmHDApSleZNo92CZBQAIB+E7HdUomfuB111DwKUpMCQDkfJ4zC3fDSP+OD1vP8Ez5jaSymsULOgAVPVwRAgA

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
