
package com.db.rest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

@Startup
@Singleton(name = "dbMigration")
@TransactionManagement(TransactionManagementType.BEAN)
public class DatabaseMigration {

    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    private DataSource dataSource;

    @PostConstruct
    private void onInit() {
        if (dataSource == null) {
            throw new EJBException(
                    "no datasource found to execute the db migrations!");
        }

        final Flyway flyway = new Flyway();

        flyway.setDataSource(dataSource);
        // flyway.setSchemas("PUBLIC");
        flyway.setBaselineOnMigrate(true);

        for (final MigrationInfo i : flyway.info().all()) {
            System.out.println("migrate task: " + i.getVersion() + " : "
                    + i.getDescription() + " from file: " + i.getScript());
        }
        flyway.migrate();
    }
}
