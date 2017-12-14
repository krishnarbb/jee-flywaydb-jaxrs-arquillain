# jee-flywaydb-jaxrs-arquillain
Showcasing the usage of JEE FlywayDB JAXRS Arquillian

Prerequsites : java 8

To run :  

cd jee-flywaydb


Build and Run the Arquillian Test
---------------------------------

mvn clean install

The above command will build the war file , create and populate the Database (H2 : InMemory) and run the arquillian test.


To deploy the WAR file to a Jboss instance running in Docker
------------------------------------------------------------
docker run -it -p 8080:8080 jee-flywaydb
docker build --no-cache --rm -t jee-flywaydb .


In the browser :
http://localhost:8080/jee-flywaydb/TestServlet

