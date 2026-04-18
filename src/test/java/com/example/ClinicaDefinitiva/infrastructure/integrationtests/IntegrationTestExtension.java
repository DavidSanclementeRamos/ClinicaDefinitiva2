package com.example.ClinicaDefinitiva.infrastructure.integrationtests;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;



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
            System.setProperty("spring.jpa.hibernate.ddl-auto", "update");
            System.setProperty("spring.jpa.show-sql", "true");
        }
    }
}
