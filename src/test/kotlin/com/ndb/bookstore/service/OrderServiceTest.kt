package com.ndb.bookstore.service

import com.ndb.bookstore.model.OrderRequest
import com.ndb.bookstore.repository.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import org.springframework.data.repository.findByIdOrNull
import java.lang.IllegalArgumentException
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

internal class OrderServiceTest {

    private lateinit var bookRepository: BookRepository
    private lateinit var customerRepository: CustomerRepository
    private lateinit var orderRepository: OrderRepository
    private lateinit var orderService: OrderService

    private val resBookOrder = generateSampleBookOrder()

    @BeforeEach
    fun setup() {
        bookRepository = mockk()
        customerRepository = mockk()
        orderRepository = mockk()
        orderService = OrderService(orderRepository, bookRepository, customerRepository)
    }

    @Nested
    inner class TestLoadOrder {

        @Test
        fun `Given a valid order id, the corresponding book order is returned`() {
            val expected = resBookOrder

            every {
                orderRepository.findByIdOrNull(any())
            } returns expected

            val res = orderService.loadOrder(1)
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given an invalid book id, an exception is thrown`() {
            every {
                orderRepository.findByIdOrNull(any())
            } returns null

            assertThrows<IllegalArgumentException> {
                orderService.loadOrder(15)
            }
        }
    }

    @Nested
    inner class TestSaveNewOrder {

        private val newOrderRequest = OrderRequest(1, 2, resBookOrder.orderDate)

        @Test
        fun `Given valid book id and customer id, the saved book order is returned`() {
            val expected = resBookOrder

            mockReposSuccess(expected)

            val res = orderService.saveNewOrder(newOrderRequest)
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given valid book id and customer id with no date, the saved book order is returned dated today`() {
            val expected = BookOrder(
                1,
                Book(1, "You", "Caroline Kepnes"),
                Customer(2, "Jane Allison"),
                ZonedDateTime.now()
            )

            mockReposSuccess(expected)

            every {
                orderRepository.save<BookOrder>(any())
            } returns expected

            val res = orderService.saveNewOrder(OrderRequest(1, 2, null))
            Assertions.assertEquals(expected.id, res.id)
            Assertions.assertEquals(expected.book, res.book)
            Assertions.assertEquals(expected.customer, res.customer)
            Assertions.assertTrue(ChronoUnit.MINUTES.between(res.orderDate, expected.orderDate) < 1)
        }

        @Test
        fun `Given an invalid book id, an exception is thrown`() {

            every {
                bookRepository.findByIdOrNull(any())
            } returns null

            assertThrows<IllegalArgumentException> {
                orderService.saveNewOrder(newOrderRequest)
            }
        }

        @Test
        fun `Given an invalid customer id, an exception is thrown`() {

            every {
                bookRepository.findByIdOrNull(any())
            } returns getSampleBook()
            every {
                customerRepository.findByIdOrNull(any())
            } returns null

            assertThrows<IllegalArgumentException> {
                orderService.saveNewOrder(newOrderRequest)
            }
        }

        private fun mockReposSuccess(expected: BookOrder) {
            every {
                bookRepository.findByIdOrNull(any())
            } returns getSampleBook()
            every {
                customerRepository.findByIdOrNull(any())
            } returns getSampleCustomer()
            every {
                orderRepository.save<BookOrder>(any())
            } returns expected
        }
    }

    @Nested
    inner class TestLoadBooksByCustomer {

        @Test
        fun `Given a valid customer id, the relevant book list is returned`() {
            val expected = listOf(Book(1, "You", "Caroline Kepnes"),
                Book(2, "You", "Charles Benoit"))

            every {
                bookRepository.queryBooksByCustomer(any())
            } returns expected

            val res = orderService.loadBooksByCustomer(1)
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given a customer id, when no books are found, an exception is thrown`() {
            every {
                bookRepository.queryBooksByCustomer(any())
            } returns emptyList()

            assertThrows<IllegalArgumentException> {
                orderService.loadBooksByCustomer(15)
            }
        }
    }
}

private fun getSampleBook() = Book(1, "You", "Caroline Kepnes")

private fun getSampleCustomer() = Customer(2, "Jane Allison")

fun generateSampleBookOrder() =
    BookOrder(
        1,
        getSampleBook(),
        getSampleCustomer(),
        ZonedDateTime.of(2020, 1, 1, 12, 12, 12, 0,
            ZoneId.from(ZoneOffset.UTC)))