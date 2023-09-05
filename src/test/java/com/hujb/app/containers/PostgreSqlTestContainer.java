package com.hujb.app.containers;

import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;

public class PostgreSqlTestContainer extends PostgreSQLContainer<PostgreSqlTestContainer> {

    public static String IMAGE_VERSION = "postgis/postgis:15-3.3";

    public static String DATABASE_NAME = "test";

    public static PostgreSQLContainer<PostgreSqlTestContainer> container;
   public PostgreSqlTestContainer(){
       super(IMAGE_VERSION);
   }

   public static PostgreSQLContainer getInstance() {
        if(container == null){
            container = new PostgreSqlTestContainer().withDatabaseName(DATABASE_NAME);
        }
        return container;
   }

    @Override
    public void start() {

       super.start();

       System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD",container.getPassword());

    }

    @Override
    public void stop() {
        super.stop();
    }
}
