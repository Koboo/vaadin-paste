# Vaadin Paste

This is a lightweight paste service. Built with Vaadin, Spring and MongoDB.

## Build and Run

Clone the project with `git clone https://github.com/Koboo/vaadin-paste`.

First set-up the MongoDB URI through `mongodb.properties`. This file is located in the root project directory.
For more documentation look [here](https://www.baeldung.com/spring-properties-file-outside-jar).

The WebServer port is also specified in this configuration file. The default port is `8080`.

After that you can build the project by executing `mvn clean package -Pproduction`.

The build artifact is located in `target/vaadinpaste-1.0-SNAPSHOT.jar`, which is started via `java -jar vaadinpaste-1.0-SNAPSHOT.jar`.

# More
- [Vaadin](https://vaadin.com/) 
- [Spring](https://spring.io/) 
- [MongoDB](https://www.mongodb.com/)