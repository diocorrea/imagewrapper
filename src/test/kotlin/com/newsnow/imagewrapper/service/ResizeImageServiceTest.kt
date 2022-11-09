package com.newsnow.imagewrapper.service

import com.newsnow.imagewrapper.domain.Task
import com.newsnow.imagewrapper.domain.TaskList
import com.newsnow.imagewrapper.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.Optional
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

class ResizeImageServiceTest {

    companion object {
        const val basePath = ".static"

        @BeforeAll
        @JvmStatic
        fun initAll() {
            Files.createDirectories(Path.of("$basePath${File.separator}tmp"))
        }

        @AfterAll
        @JvmStatic
        fun destroyAll() {
            val tmp = Path.of(basePath + File.separator + "tmp")
            val base = Path.of(basePath + File.separator + "tmp")

            if (tmp.exists()) {
                Files.list(Path.of(basePath + File.separator + "tmp")).forEach(Files::deleteIfExists)
            }
            if (base.exists()) {
                Files.list(Path.of(basePath)).forEach(Files::deleteIfExists)
            }
        }
    }

    val taskRepository = mockk<TaskRepository>()
    val baseUrl = "http://localhost"

    lateinit var serviceUnderTest: ResizeImageService

    lateinit var smallImage: Path
    lateinit var mediumImage: Path
    lateinit var largeImage: Path

    @BeforeEach
    fun init() {
        serviceUnderTest = ResizeImageService(taskRepository)
        serviceUnderTest.baseUrl = baseUrl
        serviceUnderTest.basePath = basePath
        smallImage = Path.of(this.javaClass.classLoader.getResource("small.jpg")!!.toURI())
        mediumImage = Path.of(this.javaClass.classLoader.getResource("medium.jpg")!!.toURI())
        largeImage = Path.of(this.javaClass.classLoader.getResource("large.png")!!.toURI())
    }

    @Test
    fun `should resize multiple images at the same time correctly`() {
        every { taskRepository.create(any()) }.answers { Task.TaskBuilder().build() }
        every { taskRepository.selectTaskByMd5AndWidthHeight(any(), any(), any()) }.answers { Optional.empty() }

        val w = 30
        val h = 100

        serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()),
            /* width = */ w,
            /* height = */ h
        )

        serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()),
            /* width = */ w,
            /* height = */ h
        )

        serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()),
            /* width = */ w,
            /* height = */ h
        )
        verify {
            taskRepository.create(
                withArg {
                    assertEquals(h, it.height)
                    assertEquals(w, it.width)
                }
            )
        }
    }

    @Test
    fun `should return the same response if the input is the same`() {
        every {
            taskRepository.selectTaskByMd5AndWidthHeight(
                any(),
                any(),
                any()
            )
        }.answers { Optional.of(Task.TaskBuilder().build()) }

        val w = 30
        val h = 100

        val small1 = serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()),
            /* width = */ w,
            /* height = */ h
        )
        val small2 = serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()),
            /* width = */ w,
            /* height = */ h
        )
        assertEquals(small1, small2)
        verify(exactly = 2) {
            taskRepository.selectTaskByMd5AndWidthHeight(
                withArg {
                    assertEquals("c55c201191e0bafbe8b4bcc86f1eb9be", it)
                },
                withArg { assertEquals(w, it) },
                withArg { assertEquals(h, it) }
            )
        }
    }

    @Test
    fun `should throw exception if dimension overflows`() {
        every { taskRepository.selectTaskByMd5AndWidthHeight(any(), any(), any()) }.answers { Optional.empty() }

        val w = Integer.MAX_VALUE
        val h = Integer.MAX_VALUE

        assertThrows<IllegalArgumentException> {
          serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `should throw exception if image name empty`() {
        every { taskRepository.selectTaskByMd5AndWidthHeight(any(), any(), any()) }.answers { Optional.empty() }
        val w = Integer.MAX_VALUE
        val h = Integer.MAX_VALUE

        assertThrows<IllegalArgumentException> {
            serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `should throw exception if image name in wrong format`() {
        every { taskRepository.selectTaskByMd5AndWidthHeight(any(), any(), any()) }.answers { Optional.empty() }
        val w = Integer.MAX_VALUE
        val h = Integer.MAX_VALUE

        assertThrows<IllegalArgumentException> {
           serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `should throw exception if height is null`() {
        val w = 50
        val h = null

        assertThrows<NullPointerException> {
           serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `should throw exception if width is null`() {
        val w = null
        val h = 30

        assertThrows<NullPointerException> {
            serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `should return all elements on the getall method`() {
        every { taskRepository.selectAllTasks() }.answers { listOf( Task.TaskBuilder().build()) }

        assertTrue( serviceUnderTest.getall().isPresent)
        assertEquals(1, serviceUnderTest.getall().get().tasks.size)
    }

    @Test
    fun `test md5 calculation`() {
        assertEquals(serviceUnderTest.getMd5(smallImage.toFile()), serviceUnderTest.getMd5(smallImage.toFile()))
        assertEquals(serviceUnderTest.getMd5(largeImage.toFile()), serviceUnderTest.getMd5(largeImage.toFile()))
        assertEquals(serviceUnderTest.getMd5(mediumImage.toFile()), serviceUnderTest.getMd5(mediumImage.toFile()))
    }
}
