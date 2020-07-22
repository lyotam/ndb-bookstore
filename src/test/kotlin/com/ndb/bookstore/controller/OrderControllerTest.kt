package com.ndb.bookstore.controller

import com.fasterxml.jackson.module.kotlin.readValue
import com.ndb.bookstore.model.OrderResponse
import com.ndb.bookstore.repository.Book
import com.ndb.bookstore.repository.BookOrder
import com.ndb.bookstore.service.OrderService
import com.ndb.bookstore.service.generateSampleBookOrder
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
@WebMvcTest(OrderController::class)
internal class OrderControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var orderService: OrderService

    @TestConfiguration
    class OrderControllerTestConfig {
        @Bean
        fun orderService() = mockk<OrderService>()
    }

    @Nested
    inner class TestGetOrder {

        @Test
        fun `Given valid id, an order response is returned`() {
            val bookOrder = generateSampleBookOrder()
            val expected =
                OrderResponse("You", "Caroline Kepnes", "Jane Allison", bookOrder.orderDate)

            every {
                orderService.loadOrder(any())
            } returns bookOrder

            val res = executeGetRequest(mockMvc, "/orders/1", MockMvcResultMatchers.status().isOk)
            Assertions.assertNotNull(res)
            val bookOrderRes = objectMapper.readValue<OrderResponse>(res)
            Assertions.assertEquals(expected.bookName, bookOrderRes.bookName)
            Assertions.assertEquals(expected.author, bookOrderRes.author)
            Assertions.assertEquals(expected.customerName, bookOrderRes.customerName)
            Assertions.assertTrue(expected.orderDate.isEqual(bookOrderRes.orderDate));        }

        @Test
        fun `Given invalid id, when exception is thrown by orderService, a NotFound response is returned`() {
            every {
                orderService.loadOrder(any())
            } throws IllegalArgumentException()

            executeGetRequest(mockMvc, "/orders/15", MockMvcResultMatchers.status().isNotFound)
        }
    }

    @Nested
    inner class TestCreateOrder {

        @Test
        fun `Given valid order request, the created book order is returned`() {
            val expected = generateSampleBookOrder()

            every {
                orderService.saveNewOrder(any())
            } returns expected

            val res = executePostRequest(mockMvc, "/orders",
                "{\"bookId\":\"1\",\"customerId\":\"2\",\"bookId\":\"1\"}",
                MockMvcResultMatchers.status().isCreated)
            Assertions.assertNotNull(res)
            val bookOrderRes = objectMapper.readValue<BookOrder>(res)
            Assertions.assertEquals(expected.id, bookOrderRes.id)
            Assertions.assertEquals(expected.book, bookOrderRes.book)
            Assertions.assertEquals(expected.customer, bookOrderRes.customer)
            Assertions.assertTrue(expected.orderDate.isEqual(bookOrderRes.orderDate));
        }

        @Test
        fun `Given invalid order request, when exception is thrown by orderService, a NotFound response is returned`() {
            every {
                orderService.saveNewOrder(any())
            } throws IllegalArgumentException()

            executePostRequest(mockMvc, "/orders",
                "{\"bookId\":\"15\",\"customerId\":\"2\",\"bookId\":\"1\"}",
                MockMvcResultMatchers.status().isNotFound)
        }
    }

    @Nested
    inner class TestGetOrderedBooksByCustomer {

        @Test
        fun `Given valid customerId, the corresponding book list is returned`() {
            val expected = listOf(
                Book(1, "You", "Caroline Kepnes"),
                Book(2, "You", "Charles Benoit")
            )

            every {
                orderService.loadBooksByCustomer(any())
            } returns expected

            val res = executeGetRequest(mockMvc, "/orders/customers/1",
                MockMvcResultMatchers.status().isOk)

            Assertions.assertNotNull(res)
            Assertions.assertEquals(expected, objectMapper.readValue<List<Book>>(res))
        }
    }
}