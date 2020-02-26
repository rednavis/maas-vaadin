[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6ae417c9d4ea4174969faaee919f4d33)](https://app.codacy.com/gh/rednavis/maas-vaadin?utm_source=github.com&utm_medium=referral&utm_content=rednavis/maas-vaadin&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.com/rednavis/spring-graphql-microservice.svg?branch=master)](https://travis-ci.com/rednavis/maas-vaadin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7d36295503574b40bb06bd4975dc40f6)](https://app.codacy.com/gh/rednavis/maas-vaadin?utm_source=github.com&utm_medium=referral&utm_content=rednavis/maas-vaadin&utm_campaign=Badge_Grade_Settings)
[![codecov](https://codecov.io/gh/rednavis/spring-graphql-microservice/branch/master/graph/badge.svg)](https://codecov.io/gh/rednavis/maas-vaadin)

# maas-vaadin
Client of the Material assets management system

## Technology stack
- Java 12
- Spring Boot
- Gradle
- Log4j
- Lombok
- Vaadin

## Build project
`./gradlew clean build`

### Running With Spring Boot via Gradle In Development Mode

Run the following command in this repo:

```bash
./gradlew clean vaadinPrepareFrontend bootRun
```

Now you can open the [http://localhost:8080](http://localhost:8080) with your browser.

> If you do not have node.js installed locally, please run `./gradlew vaadinPrepareNode` once.
> The task will download a local node.js distribution to your project folder, into the `node/` folder.

### Running With Spring Boot from your IDE In Development Mode

Run the following command in this repo, to create necessary Vaadin config files:

```bash
./gradlew clean vaadinPrepareFrontend
```

The `build/vaadin-generated/` folder will now contain proper configuration files.

Open the `DemoApplication` class, and Run/Debug its main method from your IDE.

Now you can open the [http://localhost:8080](http://localhost:8080) with your browser.

### Building In Production Mode

Run the following command in this repo:

```bash
./gradlew
```

That will build this app in production mode as a runnable jar archive; please find the
jar file in `build/libs/base-starter-spring-gradle*.jar`. You can run the JAR file
with:

```bash
cd build/libs/
java -jar base-starter-spring-gradle*.jar
```

Now you can open the [http://localhost:8080](http://localhost:8080) with your browser.

### Building In Production On CI

Usually the CI images will not have node.js+npm available. However, Vaadin Gradle Plugin
can download it for you. To build your app for production in CI, just run:

```bash
./gradlew clean vaadinPrepareNode vaadinBuildFrontend build
```

## Create dependency report
`./gradlew clean htmlDependencyReport`

## Create docker image
`./gradlew clean bootJar jibDockerBuild`
