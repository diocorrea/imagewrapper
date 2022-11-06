package com.newsnow.imagewrapper.service

import com.newsnow.imagewrapper.domain.Task
import com.newsnow.imagewrapper.domain.Task.TaskBuilder
import com.newsnow.imagewrapper.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import java.io.FileInputStream
import java.nio.file.Path

class ResizeImageServiceTest {

    val taskRepository = mockk<TaskRepository>()
    val baseUrl = "http://localhost"
    val basePath = ".static"

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
        every {taskRepository.create(any())}.answers { Task.TaskBuilder().build() }

        val w = 30
        val h = 100

        val small = serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()), "image.png",
            /* width = */ w,
            /* height = */ h
        )

        val medium = serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()), "image.png",
            /* width = */ w,
            /* height = */ h
        )

        val large = serviceUnderTest.resizeTask(
            /* inputStream = */ FileInputStream(smallImage.toFile()), "image.png",
            /* width = */ w,
            /* height = */ h
        )
        verify {
            taskRepository.create(withArg {
                assertEquals(h, it.height)
                assertEquals(w, it.width)
            }
            )
        }

    }


    @Test
    fun `should throw exception if dimension overflows`() {
        val w = Integer.MAX_VALUE
        val h = Integer.MAX_VALUE

        assertThrows<IllegalArgumentException> {
            val image = serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                "image.png",
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `should throw exception if image name empty`() {
        val w = Integer.MAX_VALUE
        val h = Integer.MAX_VALUE

        assertThrows<IllegalArgumentException> {
            val image = serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                "",
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `should throw exception if image name in wrong format`() {
        val w = Integer.MAX_VALUE
        val h = Integer.MAX_VALUE

        assertThrows<IllegalArgumentException> {
            val image = serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                "image",
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
            val image = serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                "image",
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
            val image = serviceUnderTest.resizeTask(
                /* inputStream = */ FileInputStream(smallImage.toFile()),
                "image",
                /* width = */ w,
                /* height = */ h
            )
        }
    }

    @Test
    fun `test md5 calculation`() {
        assertEquals(serviceUnderTest.getMd5(smallImage.toFile()), serviceUnderTest.getMd5(smallImage.toFile()))
        assertEquals(serviceUnderTest.getMd5(largeImage.toFile()), serviceUnderTest.getMd5(largeImage.toFile()))
        assertEquals(serviceUnderTest.getMd5(mediumImage.toFile()), serviceUnderTest.getMd5(mediumImage.toFile()))
    }
}
