# ♕ BYU CS 240 Chess
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIGEAtIGcnQCIgIYHMBOmBbAKCMwGNgB7XaAVSUlwHJVZwRIA7YIgB01ygyIft2gBlRgDdGfAUJGYxACSUATKLjmCQw0cAnTdkbQv3pMwTNACCZMsiRE1lzACNMDdACESRPJQArrzQAMRu4IEwAEqQ2CBIwPiglJzQRPSMLHDsXMAAtAB8krgyuABcAPSZNAA6nAAUgQy4nISQADTQ-CgA7tRqXZAEmCDgAJREJWVFqpwajOVNLW0EnT1I-biDw6MTRHMLuEXTxuXQ0LhxCcCMDVcAjlGJk6cORWiutvaO5diQwBqq0gy0YwMmnys3wcKA+3nODCgFGgwEQ0GaYPaREh1jsMKQ+RORgc5U4lFRIE42CmxMgHy+eN+ZCulkgeKC3FBrXaXQ2WzUEIZP1hhTQ8OglJaBgxNEpADNKDTSsZ6VDGShyszIKyANaQACeXPB2KF+Lh50ljAMmECqLyuksIFSJrVwoJROVJJtdu4DpSnCVUhVs3UmnK3sQvrIjudh00hOK0kWEft0f9JGmjATNWybA43HKACYAAzFhopqMxziTLhqEgBYJhPCQLjQAAylHiAZzrFy3A9ZSqDBQTrS9SNPO6nk2A1eSeOhTjiwna150-5kyXC7ekHK4E7lPukCeyGAc89dNFpt+-0BLRXnSnfVnLtxbvNiMgyJlwOgctwlAEOiLTQOoT4ztsr7Qo4CY7uUP7tKB8zgfygYqlerr4pqLK3KBtqRkIVZQeqBKivClqCMBmJrNAvSVHhqJUHqAY4tBsIDmcFaEemO4Jlu4b4amRFbrB84CT63GjoGWZFD2OT5sARaluWgmVv6NbzPWAGNqEVxqO2nZBDwcl5nkA6LJUw5IKO0DjlxfqjueMyLqGiz7tgRlHieLwHK5260nuhm2ipEkOakTnoaxJF-ACEYhQRYXVsR75keUn7IvZaZSVF74cSSCGrgxCVZc6vEYW+WFqJAUC3HFmVVoKmEwalVwEJQMhFUJ6Y5WaeW7lVNUmLxIbzGG7lGb5o0yYmpSLAA3gAvtJxyyS0uZ9opJbFkQtZaUEIShPq1X7r07Y3NAADi7ROCZG3mRUlTYIhdmqZJ4XLSNRzlMo2pVTQEYASAABewl+X1e43E9azxV1jloe85VsUg5RlCAcqGvV6nJWaqXpdar2JdjMHgwVj6Y1JZU9TeAJQ8gDSNRVzViml1VfgYtNE+xM1BiStPQOwiTw3S91-IhAs8JmK2FLdClKcW0ANHz4saXW-jaQd1BKP8cA4TAV1rBka29gpIuPc9bQEyVjS08akufWGP2YH9tj4dQwNVkh+neJQaj6pd7TApNRzg1qrK0zDamjl0NvtBFCNUxqqPoxHb1JQnpHM3jnWR866eiRe8ErJO5POkLqqMxqoe3PrkAgGoisB7HnMZ+RnBStAtN157HeNwbefg3zddl-biyD3WIki2PGbztmRvyXkcsK2PKs7Zpav7WEbgAb0aQAFKUJS-sGzLZncxZfP1CXjS1QQZCUPuuDR+0ddOVmLlTRUjvOzYru4O7-rd29r7I+XB2gAGob53wfkHeM3MzgACsD5UnaCnRKXRIH32oE-NYL8y6I2iknDGlsGrNw-KzDKxDurXi5nBUm3cr7TwvOXJGMVgDh07gKUhuNyHs0QkPfucDeZYjwencoiDKThyvug7Ut9MGPx7jgzhedUrBBcLhKRKIZFQKwRKOsAi4JkgpFSYeZ8KiGPgBIj6q0sjGwXltVeqsGwHV6BY3CbBtQ0BsLwXghsbHz37KYqoA0AQwHqGKD6gjdxkCgAIcJlNqHI2iR4rhzMkkCCotyPuCTzRpP+pQ7K2TUq5IyYHfRAVzGWOGoEipxi7bWOYLYgsNTqS7RIEAA
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
