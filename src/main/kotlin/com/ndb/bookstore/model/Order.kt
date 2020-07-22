package com.ndb.bookstore.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime

data class OrderRequest (
    val bookId: Long,
    val customerId: Long,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    val date: ZonedDateTime?
)

data class OrderResponse (
    val bookName: String,
    val author: String,
    val customerName: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    val orderDate: ZonedDateTime
)
