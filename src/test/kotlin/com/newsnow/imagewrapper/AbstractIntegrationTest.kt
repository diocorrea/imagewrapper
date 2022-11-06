package com.newsnow.imagewrapper

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import liquibase.integration.spring.SpringLiquibase
import org.junit.ClassRule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode  = DirtiesContext.ClassMode.BEFORE_CLASS)
class AbstractIntegrationTest {

    companion object {
        @ClassRule
        @JvmStatic
        val postgreSQLContainer = PostgreSQLContainer("postgres:13.8")
            .withDatabaseName("image-wrapper-db")
            .withExposedPorts(5432)
            .withUsername("user")
            .withPassword("pass")

        @JvmStatic
        @ClassRule
        @DynamicPropertySource
        fun init(registry: DynamicPropertyRegistry) {
            postgreSQLContainer.start()
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
        }



        @AfterAll
        @JvmStatic
        fun destroy() {
            postgreSQLContainer.stop()
        }
    }

}
