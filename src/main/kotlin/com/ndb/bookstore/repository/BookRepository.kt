package com.ndb.bookstore.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import javax.persistence.*

@Entity(name = "books")
data class Book (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val author: String
)

interface BookRepository : CrudRepository<Book, Long> {

    @Query("SELECT os.book FROM orders AS os LEFT JOIN os.customer AS cm WHERE cm.id = :customerId")
    fun queryBooksByCustomer(@Param("customerId") customerId: Long): List<Book>

    @Query("SELECT * FROM books bs WHERE bs.name = :name", nativeQuery = true)
    fun queryBooksByName(@Param("name") name: String): List<Book>

    @Query("SELECT * FROM books bs WHERE bs.author = :author", nativeQuery = true)
    fun queryBooksByAuthor(@Param("author") author: String): List<Book>
}