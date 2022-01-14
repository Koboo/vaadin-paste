# Vaadin Paste

This is a lightweight paste service. Built with Vaadin, Spring and MongoDB.

## Build and Run

Clone the project with `git clone https://github.com/Koboo/vaadin-paste`.

Go into the project directory and run `mvn clean package -Pproduction`.

The build artifact is located in `target/vaadinpaste-1.0-SNAPSHOT.jar`.

Now you need to set up the port and MongoDB-URI through the `paste.properties` file. This file is located in the application/project directory and looks like this:
````
# Configure the webserver port of Apache Tomcat
server.port=${PORT:<your port>}

# Configure MongoDB database access via URI e.g. "mongodb+srv://<username>:<password>@<host[:port]>/<database>?retryWrites=true&w=majority"
spring.data.mongodb.uri=<mongodb-uri>
spring.data.mongodb.database=<database-name>

# Configure how long a paste remains stored.
paste.days=14
````
Just create a new file named `paste.properties` and fill the properties.

For more properties-related documentation look [here](https://www.baeldung.com/spring-properties-file-outside-jar) and [here](https://stackoverflow.com/questions/23515295/spring-boot-and-how-to-configure-connection-details-to-mongodb/34373673#34373673).

If you finished the build and configuration steps you can run the application through `java -jar vaadinpaste-1.0-SNAPSHOT.jar`.

# Good to know

You can get a free MongoDB Atlas Cluster. Just register an account [here](https://www.mongodb.com/atlas/database).

Please note: These are only for testing or demo purposes and should not be used in production!

# Links
- [Vaadin](https://vaadin.com/)
- [Vaadin AceEditor](https://vaadin.com/directory/component/ace)
- [Vaadin AceEditor GitHub](https://github.com/F0rce/ace)
- [Spring](https://spring.io/) 
- [MongoDB](https://www.mongodb.com/)