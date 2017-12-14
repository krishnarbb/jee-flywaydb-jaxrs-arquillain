
package com.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.db.rest.DatabaseMigration;
import com.db.rest.Employee;
import com.db.rest.EmployeeBean;
import com.db.rest.EmployeeResource;
import com.db.rest.EntityManagerProducer;
import com.db.rest.JAXRSConfiguration;
import com.db.rest.TestServlet;

@RunWith(Arquillian.class)
public class EmployeeResourceTest {

    private WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        final File h2Library = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("com.h2database:h2").withoutTransitivity()
                .asSingleFile();
        final File flywaydbLibrary = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.flywaydb:flyway-core").withoutTransitivity()
                .asSingleFile();

        return ShrinkWrap.create(WebArchive.class, "EmployeeRestWar.war")
                .addAsLibraries(h2Library)
                .addAsLibraries(flywaydbLibrary)
                .addClasses(JAXRSConfiguration.class, TestServlet.class,
                        DatabaseMigration.class, Employee.class,
                        EmployeeBean.class, EmployeeResource.class, EntityManagerProducer.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("db/migration/V1__create.sql")
                .addAsResource("db/migration/V2__load.sql")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {
        final Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "resources/employee").toExternalForm()));
        target.register(Employee.class);
    }

    @Test
    @RunAsClient
    public void testGet() {
        final Employee[] list = target
                .request(MediaType.APPLICATION_XML)
                .get(Employee[].class);
        assertNotNull(list);
        assertEquals(8, list.length);

        assertFalse(list[0].equals(new Employee("Elsa")));
        assertFalse(list[1].equals(new Employee("Anna")));
        assertFalse(list[2].equals(new Employee("Elsia")));
        assertFalse(list[3].equals(new Employee("Annia")));
        assertFalse(list[4].equals(new Employee("Chelse")));
        assertFalse(list[5].equals(new Employee("Isabel")));
        assertFalse(list[6].equals(new Employee("Anabel")));
        assertFalse(list[7].equals(new Employee("Tricksy")));
    }

    @Test
    @RunAsClient
    public void should_return_http_ok() throws Exception {

        final int code = target
                .request(MediaType.APPLICATION_XML)
                .get().getStatus();
        assertEquals(code, 200);
    }

}
