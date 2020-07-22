package com.ndb.bookstore.controller

import com.ndb.bookstore.model.OrderRequest
import com.ndb.bookstore.model.OrderResponse
import com.ndb.bookstore.repository.Book
import com.ndb.bookstore.repository.BookOrder
import com.ndb.bookstore.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class OrderController (
    private val orderService: OrderService
) {
    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getOrder(@PathVariable id: Long): OrderResponse {
        orderService.loadOrder(id).also { bo -> return OrderResponse(bo.book.name, bo.book.author, bo.customer.name, bo.orderDate) }
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody order: OrderRequest): BookOrder {
        return orderService.saveNewOrder(order)
    }

    @GetMapping("/orders/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getOrderedBooksByCustomer(@PathVariable id: Long): List<Book> {
        return orderService.loadBooksByCustomer(id)
    }
}