package com.hujb.app.config;


import com.hujb.app.containers.PostgreSqlTestContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnviroment {

    @Container

    public static PostgreSQLContainer postgreSQLContainer = PostgreSqlTestContainer.getInstance();
}
