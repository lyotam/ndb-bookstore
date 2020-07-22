package com.ndb.bookstore.controller

import com.ndb.bookstore.repository.Customer
import com.ndb.bookstore.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class CustomerController (
    private val customerService: CustomerService
) {
    @GetMapping("/customers")
    @ResponseStatus(HttpStatus.OK)
    fun getAllCustomers(): List<Customer> {
        return customerService.loadAllCustomers()
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    fun addCustomer(@RequestBody customerName: String): Customer {
        return customerService.saveNewCustomer(customerName)
    }
}