package com.example.ClinicaDefinitiva.infrastructure.integrationtests;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class IntegrationTestExtension implements BeforeAllCallback {
    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("clinica_test")
            .withUsername("test")
            .withPassword("test");

    @Override
    public void beforeAll(ExtensionContext context) {
        if(!mysql.isRunning()) {
            mysql.start();
            System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
            System.setProperty("spring.datasource.username", mysql.getUsername());
            System.setProperty("spring.datasource.password", mysql.getPassword());
            System.setProperty("spring.jpa.generate-ddl", "true");
            System.setProperty("spring.jpa.hibernate.ddl-auto", "create-drop");
            System.setProperty("spring.jpa.show-sql", "true");
        }
        cleanDatabase(context);
    }
    private void cleanDatabase(ExtensionContext context) {
        ApplicationContext springContext = SpringExtension.getApplicationContext(context);
        DataSource dataSource = springContext.getBean(DataSource.class);

        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            ResultSet rs = statement.executeQuery("SHOW TABLES");
            List<String> tables = new ArrayList<>();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }

            for (String table : tables) {
                statement.execute("TRUNCATE TABLE " + table);
            }

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean database before test", e);
        }
    }
}
