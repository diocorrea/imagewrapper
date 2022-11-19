package com.newsnow.imagewrapper

import com.newsnow.imagewrapper.service.ResizeImageServiceTest
import org.junit.ClassRule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class AbstractIntegrationTest {

    companion object {
        @ClassRule
        @JvmStatic
        val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:13.8")
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

            val tmp = Path.of(ResizeImageServiceTest.basePath + File.separator + "tmp")
            val base = Path.of(ResizeImageServiceTest.basePath + File.separator + "tmp")

            if (tmp.exists()) {
                Files.list(Path.of(ResizeImageServiceTest.basePath + File.separator + "tmp")).forEach(Files::deleteIfExists)
            }
            if (base.exists()) {
                Files.list(Path.of(ResizeImageServiceTest.basePath)).forEach(Files::deleteIfExists)
            }
        }
    }
}
