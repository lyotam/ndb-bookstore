package com.ndb.bookstore.service

import com.ndb.bookstore.repository.*
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class CustomerService (
    private val customerRepository: CustomerRepository
) {
    fun loadAllCustomers(): List<Customer> =
        customerRepository.findAll()

    fun saveNewCustomer(customerName: String): Customer =
        if (customerName.isNotEmpty()) customerRepository.save(Customer(name = customerName))
        else throw IllegalArgumentException()
}