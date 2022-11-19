package com.newsnow.imagewrapper.repository

import com.newsnow.imagewrapper.AbstractIntegrationTest
import com.newsnow.imagewrapper.domain.Task.TaskBuilder
import com.newsnow.imagewrapper.repository.generated.Tables
import org.jooq.DSLContext
import org.junit.Ignore
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class TaskRepositoryIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    lateinit var taskRepository: TaskRepository

    @Autowired
    lateinit var dsl: DSLContext

    @Test
    @Ignore
    fun `test simple select`() {
        taskRepository.create(
            TaskBuilder().fileName("filename")
                .height(10)
                .width(10)
                .md5("asdfasdfasdfasdf")
                .url("http://localhost:8080/task/someimage.gif").build()
        )
        taskRepository.create(
            TaskBuilder().fileName("filename")
                .height(10)
                .width(10)
                .md5("asdfasdfasdfasdf")
                .created(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .url("http://localhost:8080/task/someimage.gif").build()
        )

        assertEquals(2, taskRepository.selectAllTasks().size)
    }

    @Test
    fun `save task into database`() {
        val task = TaskBuilder().fileName("filename")
            .height(10)
            .width(10)
            .md5("asdfasdfasdfasdf")
            .url("http://localhost:8080/task/someimage.gif").build()

        val inserted = taskRepository.create(task)

        assertNotNull(inserted.id)
        assertNotNull(inserted.created)

        task.id = inserted.id
        task.created = inserted.created

        assertEquals(task.toString(), inserted.toString())
        assertEquals(task, inserted)
    }

    @Test
    fun `find by md5 height and width`() {
        val task = TaskBuilder()
            .id(UUID.randomUUID())
            .fileName("filename")
            .height(10)
            .width(10)
            .md5("asdfasdfasdfasdf")
            .url("http://localhost:8080/task/someimage.gif").build()

        val inserted = taskRepository.create(task)

        val selected =
            taskRepository.selectTaskByMd5AndWidthHeight(inserted.md5, inserted.width, inserted.height)

        assertTrue(selected.isPresent)
        assertEquals(inserted.url, selected.get().url)
        assertEquals(inserted.height, selected.get().height)
        assertEquals(inserted.width, selected.get().width)
        assertEquals(inserted.fileName, selected.get().fileName)
    }

    @AfterEach
    fun cleanUpDb() {
        dsl.deleteFrom(Tables.TASK).execute()
    }
}
