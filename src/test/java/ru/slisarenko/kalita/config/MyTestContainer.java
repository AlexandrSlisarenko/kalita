package ru.slisarenko.kalita.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MyTestContainer {

    private static final Logger log = LoggerFactory.getLogger(MyTestContainer.class);


    @Bean
    @ServiceConnection
    public PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:17-alpine")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres")
                .withLogConsumer(new Slf4jLogConsumer(log).withPrefix("POSTGRES"));
    }

}
