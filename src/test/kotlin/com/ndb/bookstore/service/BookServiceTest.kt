package com.ndb.bookstore.service

import com.ndb.bookstore.repository.Book
import com.ndb.bookstore.repository.BookRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import org.springframework.data.repository.findByIdOrNull
import java.lang.IllegalArgumentException

internal class BookServiceTest {

    private lateinit var bookRepository: BookRepository
    private lateinit var bookService: BookService

    @BeforeEach
    fun setup() {
        bookRepository = mockk()
        bookService = BookService(bookRepository)
    }

    @Nested
    inner class TestLoadBooks {

        private val sameTitleBooks: List<Book> = generateSameTitleBooks()
        private val sameAuthorBooks: List<Book> = generateSameAuthorBooks()

        @Test
        fun `Given a valid book id, a single book list is returned`() {
            val expected = sameTitleBooks[0]

            every {
                bookRepository.findByIdOrNull(any())
            } returns expected

            val res = bookService.loadBooks(1, null, null)
            Assertions.assertEquals(1, res.size)
            Assertions.assertEquals(listOf(expected), res)
        }

        @Test
        fun `Given an invalid book id, an exception is thrown`() {
            every {
                bookRepository.findByIdOrNull(any())
            } returns null

            assertThrows<IllegalArgumentException> {
                bookService.loadBooks(15, null, null)
            }
        }

        @Test
        fun `Given a valid book name, a single book list is returned`() {
            val expected = listOf(sameAuthorBooks[0])

            every {
                bookRepository.queryBooksByName(any())
            } returns expected

            val res = bookService.loadBooks(null, "The Catcher in the Rye", null)
            Assertions.assertEquals(1, res.size)
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given a valid book name, a book list is returned`() {
            val expected = sameTitleBooks

            every {
                bookRepository.queryBooksByName(any())
            } returns expected

            val res = bookService.loadBooks(null, "You", null)
            Assertions.assertEquals(2, res.size)
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given an invalid book name, an exception is thrown`() {
            every {
                bookRepository.queryBooksByName(any())
            } returns emptyList()

            assertThrows<IllegalArgumentException> {
                bookService.loadBooks(null, "Books for Dummies", null)
            }
        }

        @Test
        fun `Given a valid author name, a single book list is returned`() {
            val expected = listOf(sameTitleBooks[0])

            every {
                bookRepository.queryBooksByAuthor(any())
            } returns expected

            val res = bookService.loadBooks(null, null, "Caroline Kepnes")
            Assertions.assertEquals(1, res.size)
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given a valid book author, a book list is returned`() {
            val expected = sameAuthorBooks

            every {
                bookRepository.queryBooksByAuthor(any())
            } returns expected

            val res = bookService.loadBooks(null, null, "J. D. Salinger")
            Assertions.assertEquals(2, res.size)
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given an invalid book author, an exception is thrown`() {
            every {
                bookRepository.queryBooksByAuthor(any())
            } returns emptyList()

            assertThrows<IllegalArgumentException> {
                bookService.loadBooks(null, null, "Jane Doe")
            }
        }

        @Test
        fun `Given no arguments, an empty list is returned`() {
            val res = bookService.loadBooks(null, null, null)
            Assertions.assertEquals(0, res.size)
            Assertions.assertEquals(emptyList<Book>(), res)
        }

        private fun generateSameTitleBooks() =
            listOf(Book(1, "You", "Caroline Kepnes"),
                Book(2, "You", "Charles Benoit"))

        private fun generateSameAuthorBooks() =
            listOf(Book(3, "The Catcher in the Rye", "J. D. Salinger"),
                Book(4, "Nine Stories", "J. D. Salinger"))
    }

    @Nested
    inner class TestSaveNewBook {

        @Test
        fun `Given a valid book, the saved book is returned`() {
            val expected = Book(3,"The Catcher in the Rye", "J. D. Salinger")

            every {
                bookRepository.save<Book>(any())
            } returns expected

            val res = bookService.saveNewBook(Book(name = "The Catcher in the Rye", author = "J. D. Salinger"))
            Assertions.assertEquals(expected, res)
        }
    }
}