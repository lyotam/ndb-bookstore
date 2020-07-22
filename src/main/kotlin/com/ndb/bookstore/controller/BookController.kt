package com.ndb.bookstore.controller

import com.ndb.bookstore.repository.Book
import com.ndb.bookstore.repository.BookRepository
import com.ndb.bookstore.service.BookService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class BookController (
    private val bookService: BookService
) {
    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    fun getBooks(@RequestParam(required = false) id: Long?, @RequestParam(required = false) name: String?,
                 @RequestParam(required = false) author: String?): List<Book> {
        return bookService.loadBooks(id, name, author)
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody book: Book): Book {
        return bookService.saveNewBook(book)
    }
}