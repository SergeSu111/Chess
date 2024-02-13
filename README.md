# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

### Chess Server Design's URL
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpZjngUSWDAEooDmSAzmFMAQhoK1Qinr5CxYKQAiA4AEEQIFN26YAJgoBGwbihiaddCY2mk4ACxkcUAWRQAWTBygQArthgBiNMCoATzZOHj4BJCFfAHcrJDBxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAeg8DKAAdNABvOsp-AFsUABoYXHVo6E1elA7gJAQAX0wRbJh0kK5efkFhLLE8qFDeSgAKNqhOnr79bkGoYZhR8YQASkx2JfDV2bF5jPkwJRU1bjy7MAAVXq+3qR3un2+qnU7yMOjyZAAogAZRFwMowA5HGAAM3cHUx9UwkOU0O48wWjzCK0ia1EqjyaA8CAQD22z1pr1UsJJP3UeRAWwEKGBeyxwC6vX6ZyGEIUpN+sOMeQAkgA5JGsDHiyUnAZDEZjCYwdVlYqE2hU5YRIRcwwLXlkgVChKKDxgKyg9oSlByr4KmELZUmjWIrUWw4+3rAd1WMoQADW6BDZpgMY9xPlfPJKUp7JptvWDLTsfjSbQbKeBbpVDeuYys0oeXTccT6BmWUoFIySXQYDyACYAAxDprNFtl9DTdCaOhuTzeHzQWyGZEQLhoGJxBKYXupBud3IFErlKrVAzqWljnXHaXnTTTRuwdJWjmF+koPIIddINBeyO6nesqVtSNo1nWaSOr8-woECII3lKpz3vcr7VnaPJZk6MCCigwpuh6-7gpm-rZu8ypIqi6IRkciH6hcuL4hGxFQoqQbwiGmramCUYlh6k6bqa5otsxAY5i++ZgXazalm2FaoZJRb2ukT45BOskdtQXb1nu-YwMOo4tGp5bTJgM5zu4Xi+G4KDJmuHCeCwPixPEiTIH2sIqQUsgooiZSImeF7cFe-gyeWGlzOJVYKR+X7rg5uxGegKESS8invFB-LZEgOKBPxCWhUlIHWqlH7pZh0GaCgCCwSg+GeolaD3CJpFsXk3mon5vGtuWDEQASdWQuFWkLDpOT6XQZmuBZi5bJoMDImEMAAOI+uSTnbq5yQsAsnn5EtvlnhwPpNA1Q3Phk8klbWxbILwK1dNw+V8bJyVRVdEEwBlfwwFlOV5Q1fosYGGTBhRaIYkdXQqrIvTOQkooAccOgIKACYIzRMCQygao+r1BL3Sgg1fd2iyge9xZoBALBMiymCk8VnJpfWmSaUezS3SwEA4pjq3TDALMRSNbkYHk+T6VUzQwAARFj3BS6LzQyz60PyzAACMA4AMxOL0UtwyK3FdPLUtS7ryOo+jPrG6b0tYzjXQ5CbkwwBUJmTfOlk+NgHhQNg1XwC6hgE1uLm7sL20HqzosnpUNRYydBVoGOds+o+h7vJdjMxT9lDZYEdX-Ynr1k1n132h85X8thgcE7sKddIDolKux6qcTzXT24YeJ9ct-AdETldiRdKWlxsMDNFj0N85n75lyTnkT8rsh8-zAvDT24ei+L4+20vqsa9rMDTmgs5TQuviBFV37RDAABSEC-stuM+ObIAJmHW0eYe0eArH1TxyFZ65Yxw6TgBAb8UBeiT2XmdDOI9Z5j1+rlWST1uqFRnuBbkDpB4CisCgN+BNEQAA8wiPWgY3FqIN2JgyotAvGT8ugDxImSEmGCpLt3EGwpmkEcFeG0AkJafcUC7FAeA6AUCl4UJYa1VuYYMRJDQLBMBECJFQ1kPQgmg0uGlXrE+WB2lN56RHKZE+5lz4+DcMAYIiBcKwGANgP2hAwIhx3DpL+UcvI+T8meYwMB9FpG0WXHIIBqp4F2MXBmCCsE8OYdBEJtjwnNWkWkZUrBET2GKAANURBw7gvQDh5K6vxck3cCTGH0ekUaRihwmM0EAA
