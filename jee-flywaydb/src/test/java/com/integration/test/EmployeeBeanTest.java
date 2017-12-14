
package com.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.db.rest.DatabaseMigration;
import com.db.rest.Employee;
import com.db.rest.EmployeeBean;
import com.db.rest.EntityManagerProducer;
import com.db.rest.JAXRSConfiguration;
import com.db.rest.TestServlet;

@RunWith(Arquillian.class)
public class EmployeeBeanTest {

    @Inject
    EmployeeBean bean;

    @Deployment
    public static WebArchive createDeployment() {
        final File h2Library = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("com.h2database:h2").withoutTransitivity()
                .asSingleFile();
        final File flywaydbLibrary = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.flywaydb:flyway-core").withoutTransitivity()
                .asSingleFile();

        return ShrinkWrap.create(WebArchive.class, "EmployeeBeanTestWar.war")
                .addAsLibraries(h2Library)
                .addAsLibraries(flywaydbLibrary)
                .addClasses(JAXRSConfiguration.class, TestServlet.class,
                        DatabaseMigration.class, Employee.class,
                        EmployeeBean.class, EntityManagerProducer.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("db/migration/V1__create.sql")
                .addAsResource("db/migration/V2__load.sql")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testGet() throws Exception {
        System.out.println("Starting test : shouldApplyMigrations");
        assertNotNull(bean);
        final List<Employee> list = bean.get();
        assertNotNull(list);
        assertEquals(8, list.size());
        assertFalse(list.contains(new Employee("Elsa")));
        assertFalse(list.contains(new Employee("Anna")));
        assertFalse(list.contains(new Employee("Elsia")));
        assertFalse(list.contains(new Employee("Annia")));
        assertFalse(list.contains(new Employee("Chelse")));
        assertFalse(list.contains(new Employee("Isabel")));
        assertFalse(list.contains(new Employee("Anabel")));
        assertFalse(list.contains(new Employee("Tricksy")));
        System.out.println("Finished test : shouldApplyMigrations");
    }

}
