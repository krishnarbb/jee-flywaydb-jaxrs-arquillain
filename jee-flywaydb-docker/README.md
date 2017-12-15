# jee-flywaydb-jaxrs-arquillain
Showcasing the usage of Automated Arquillian tests.

Prerequsites : java 8

To run :  

cd jee-flywaydb


Jboss instance running in Docker
--------------------------------
Run the below commands in a different shell.

docker build --no-cache --rm -t jee-flywaydb .

docker run -it -p 8080:8080 -p 9990:9990  -v /tmp/deployments:/opt/wildfly-9.0.1.Final/standalone/deployments/:rw jee-flywaydb



Build and Run the Arquillian Test
---------------------------------
Run the Tests from a different shell.
mvn clean install

The above command will build the war file , create and populate the Database (H2 : InMemory) and run the arquillian test.



Docker Cleanup after the tests are completed.
---------------------------------------------
# stop ALL containers 
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

