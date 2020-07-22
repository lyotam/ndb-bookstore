package com.ndb.bookstore.repository

import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity(name = "customers")
data class Customer (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String
)

interface CustomerRepository : CrudRepository<Customer, Long> {
    override fun findAll(): List<Customer>
}