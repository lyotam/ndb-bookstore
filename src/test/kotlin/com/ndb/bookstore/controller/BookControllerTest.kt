package com.ndb.bookstore.controller

import com.fasterxml.jackson.module.kotlin.readValue
import com.ndb.bookstore.repository.Book
import com.ndb.bookstore.service.BookService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.IllegalArgumentException

@ExtendWith(SpringExtension::class)
@WebMvcTest(BookController::class)
internal class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookService: BookService

    @TestConfiguration
    class BookControllerTestConfig {
        @Bean
        fun bookService() = mockk<BookService>()
    }

    @Nested
    inner class TestGetBooks {

        @Test
        fun `Given request params, a book list is returned`() {
            val expected = listOf(
                Book(1, "You", "Caroline Kepnes"),
                Book(2, "You", "Charles Benoit")
            )

            every {
                bookService.loadBooks(any(), any(), any())
            } returns expected

            val res = executeGetRequest(mockMvc, "/books?name=You", MockMvcResultMatchers.status().isOk)
            Assertions.assertNotNull(res)
            Assertions.assertEquals(expected, objectMapper.readValue<List<Book>>(res))
        }

        @Test
        fun `Given request params, when exception is thrown by bookService, a NotFound response is returned`() {
            every {
                bookService.loadBooks(any(), any(), any())
            } throws IllegalArgumentException()

            executeGetRequest(mockMvc, "/books", MockMvcResultMatchers.status().isNotFound)
        }
    }

    @Nested
    inner class TestSaveBook {

        @Test
        fun `Given valid Book, the created book is returned`() {
            val expected = Book(1, "You", "Caroline Kepnes")

            every {
                bookService.saveNewBook(any())
            } returns expected

            val res = executePostRequest(mockMvc, "/books",
                "{\"name\":\"You\", \"author\":\"Caroline Kepnes\"}", MockMvcResultMatchers.status().isCreated)
            Assertions.assertNotNull(res)
            Assertions.assertEquals(expected, objectMapper.readValue<Book>(res))
        }
    }
}