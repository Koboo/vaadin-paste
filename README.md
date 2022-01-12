# Vaadin Paste

This is a lightweight paste service. Built with Vaadin, Spring and MongoDB.

## Build and Run

Clone the project with `git clone https://github.com/Koboo/vaadin-paste`.

Go into the project directory and run `mvn clean package -Pproduction`.

The build artifact is located in `target/vaadinpaste-1.0-SNAPSHOT.jar`.

Now you need to set up the port and MongoDB-URI through the `paste.properties` file. This file is located in the application/project directory and looks like this:
````
server.port=${PORT:<your port>}

spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<host[:port]>/<database>?retryWrites=true&w=majority
spring.data.mongodb.database=<database-name>
````
Just create a new file named `paste.properties` and fill the properties.

For more properties-related documentation look [here](https://www.baeldung.com/spring-properties-file-outside-jar) and [here](https://stackoverflow.com/questions/23515295/spring-boot-and-how-to-configure-connection-details-to-mongodb/34373673#34373673).

If you finished the build and configuration steps you can run the application through `java -jar vaadinpaste-1.0-SNAPSHOT.jar`.

# Good to know

You can get a free MongoDB Atlas Cluster. Just register [here](https://www.mongodb.com/atlas/database).

Please note: These are only for testing or demo purposes and should not be used in production!

# Links
- [Vaadin](https://vaadin.com/) 
- [Spring](https://spring.io/) 
- [MongoDB](https://www.mongodb.com/)