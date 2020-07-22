package com.ndb.bookstore.repository

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.repository.CrudRepository
import java.time.ZonedDateTime
import javax.persistence.*

@Entity(name = "orders")
data class BookOrder (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = ForeignKey(name = "BOOK_ID_FK"), nullable = false)
    val book: Book,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = ForeignKey(name = "CUSTOMER_ID_FK"), nullable = false)
    val customer: Customer,

    @Column(name = "order_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    val orderDate: ZonedDateTime
)

interface OrderRepository : CrudRepository<BookOrder, Long>