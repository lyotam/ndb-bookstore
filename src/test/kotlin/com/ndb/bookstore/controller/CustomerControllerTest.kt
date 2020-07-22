package com.ndb.bookstore.controller

import com.fasterxml.jackson.module.kotlin.readValue
import com.ndb.bookstore.repository.Customer
import com.ndb.bookstore.service.CustomerService
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
@WebMvcTest(CustomerController::class)
internal class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerService: CustomerService

    @TestConfiguration
    class CustomerControllerTestConfig {
        @Bean
        fun customerService() = mockk<CustomerService>()
    }

    @Nested
    inner class TestGetAllCustomers {

        @Test
        fun `Given request, a customer list is returned`() {
            val expected = listOf(Customer(1, "John Ivory"), Customer(2, "Jane Allison"))

            every {
                customerService.loadAllCustomers()
            } returns expected

            val res = executeGetRequest(mockMvc, "/customers", MockMvcResultMatchers.status().isOk)
            Assertions.assertNotNull(res)
            Assertions.assertEquals(expected, objectMapper.readValue<List<Customer>>(res))
        }
    }

    @Nested
    inner class TestAddCustomer {

        @Test
        fun `Given valid name, the created customer is returned`() {
            val expected = Customer(1, "John Ivory")

            every {
                customerService.saveNewCustomer(any())
            } returns expected

            val res = executePostRequest(mockMvc, "/customers",
                "{\"customerName\":\"John Ivory\"}", MockMvcResultMatchers.status().isCreated)
            Assertions.assertNotNull(res)
            Assertions.assertEquals(expected, objectMapper.readValue<Customer>(res))
        }

        @Test
        fun `Given invalid name, when exception is thrown by customerService, a NotFound response is returned`() {
            every {
                customerService.saveNewCustomer(any())
            } throws IllegalArgumentException()

            executePostRequest(mockMvc, "/customers",
                "{\"customerName\":\"\"}", MockMvcResultMatchers.status().isNotFound)
        }
    }
}