package com.ndb.bookstore.service

import com.ndb.bookstore.model.OrderRequest
import com.ndb.bookstore.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class OrderService (
    private val orderRepository: OrderRepository,
    private val bookRepository: BookRepository,
    private val customerRepository: CustomerRepository
) {
    fun loadOrder(id: Long): BookOrder {
        return orderRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("Order not found")
    }

    fun saveNewOrder(order: OrderRequest): BookOrder {
        return orderRepository.save(
            BookOrder(
                book = bookRepository.findByIdOrNull(order.bookId)
                    ?: throw IllegalArgumentException("Book not found"),
                customer = customerRepository.findByIdOrNull(order.customerId)
                    ?: throw IllegalArgumentException("Customer not found"),
                orderDate = order.date ?: ZonedDateTime.now()
            )
        )
    }

    fun loadBooksByCustomer(customerId: Long): List<Book> {
        return bookRepository.queryBooksByCustomer(customerId)
            .ifEmpty { throw IllegalArgumentException("No Books found ordered by customer with id: $customerId") }
    }
}