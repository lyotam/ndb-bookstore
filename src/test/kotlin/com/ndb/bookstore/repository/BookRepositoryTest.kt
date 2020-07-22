package com.ndb.bookstore.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
internal class BookRepositoryTest {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Nested
    inner class TestQueryBooksByCustomer {

        @Test
        fun `Given valid customer id, a book list is returned`() {
            val books1 = listOf(
                Book(name = "You", author = "Caroline Kepnes"),
                Book(name = "The Catcher in the Rye", author = "J. D. Salinger"))
            val books2 = listOf(Book(name = "Nine Stories", author = "J. D. Salinger"))
            val customers = listOf(Customer(name = "John Ivory"), Customer(name = "Jane Allison"))

            val orders = listOf(
                BookOrder(book = books1[0], customer = customers[0],
                    orderDate = ZonedDateTime.of(2020, 1, 1, 12, 12, 12, 0,
                        ZoneId.from(ZoneOffset.UTC))),
                BookOrder(book = books1[1], customer = customers[0],
                    orderDate = ZonedDateTime.of(2020, 1, 1, 12, 12, 12, 0,
                        ZoneId.from(ZoneOffset.UTC))),
                BookOrder(book = books2[0], customer = customers[1],
                    orderDate = ZonedDateTime.of(2019, 1, 1, 2, 24, 0, 0,
                        ZoneId.from(ZoneOffset.UTC))))

            bookRepository.saveAll(books1)
            bookRepository.saveAll(books2)
            customerRepository.saveAll(customers)
            orderRepository.saveAll(orders)

            val res = bookRepository.queryBooksByCustomer(customers[0].id)
            Assertions.assertEquals(books1, res)
        }
    }
}