package com.ndb.bookstore.service

import com.ndb.bookstore.repository.Customer
import com.ndb.bookstore.repository.CustomerRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import java.lang.IllegalArgumentException

internal class CustomerServiceTest {

    private lateinit var customerRepository: CustomerRepository
    private lateinit var customerService: CustomerService

    @BeforeEach
    fun setup() {
        customerRepository = mockk()
        customerService = CustomerService(customerRepository)
    }

    @Nested
    inner class TestLoadAllCustomers {

        @Test
        fun `Given a call, a customer list is returned`() {
            val expected = listOf(Customer(1, "John Ivory"), Customer(2, "Jane Allison"))

            every {
                customerRepository.findAll()
            } returns expected

            val res = customerService.loadAllCustomers()
            Assertions.assertEquals(expected, res)
        }
    }

    @Nested
    inner class TestSaveNewCustomer {

        @Test
        fun `Given a valid name, the saved customer is returned`() {
            val expected = Customer(2, "Jane Allison")

            every {
                customerRepository.save<Customer>(any())
            } returns expected

            val res = customerService.saveNewCustomer("Jane Allison")
            Assertions.assertEquals(expected, res)
        }

        @Test
        fun `Given an empty name, an exception is thrown`() {
            assertThrows<IllegalArgumentException> {
                customerService.saveNewCustomer("")
            }
        }
    }
}