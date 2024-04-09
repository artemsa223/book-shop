package com.example.demo.config;

import org.testcontainers.containers.MySQLContainer;

public class MySqlContainer extends MySQLContainer<MySqlContainer> {
    private static final String DB_IMAGE = "mysql:8";

    private static MySqlContainer mysqlContainer;

    private MySqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized MySqlContainer getInstance() {
        if (mysqlContainer == null) {
            mysqlContainer = new MySqlContainer();
        }
        return mysqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mysqlContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mysqlContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mysqlContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
