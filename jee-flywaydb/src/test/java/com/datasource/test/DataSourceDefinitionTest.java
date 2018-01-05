
package com.datasource.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
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
public class DataSourceDefinitionTest {
    @Deployment
    public static Archive<?> deploy() {
        final File h2Library = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("com.h2database:h2").withoutTransitivity()
                .asSingleFile();
        final File flywaydbLibrary = Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.flywaydb:flyway-core").withoutTransitivity()
                .asSingleFile();

        return ShrinkWrap.create(WebArchive.class, "DataSourceDefinitionTestWAR.war")
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

    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    DataSource dataSource;

    @Test
    public void should_bean_be_injected() throws Exception {
        assertThat(dataSource, is(notNullValue()));
        assertThat(dataSource.getConnection(), is(notNullValue()));
    }

    @Test
    public void shouldApplyMigrations() throws Exception {
        System.out.println("Starting test : shouldApplyMigrations");
        final Connection connection = dataSource.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from \"EMPLOYEE_SCHEMA\"");
                ResultSet rs = preparedStatement.executeQuery()) {
            rs.next();
            final int anInt = rs.getInt(1);
            assertEquals(8, anInt);
        }
    }

}
