package com.ndb.bookstore.service

import com.ndb.bookstore.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookService (
    private val bookRepository: BookRepository
) {
    fun loadBooks(id: Long?, name: String?, author: String?): List<Book> {
        var res = emptyList<Book>()

        if (id != null)
            res = listOf(bookRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("Book not found"))
        else if (!name.isNullOrEmpty())
            res = bookRepository.queryBooksByName(name)
                .ifEmpty { throw IllegalArgumentException("No Books found with name: $name") }
        else if (!author.isNullOrEmpty())
            res = bookRepository.queryBooksByAuthor(author)
                .ifEmpty { throw IllegalArgumentException("No Books found with author: $author") }
        return res
    }

    fun saveNewBook(book: Book): Book {
        return bookRepository.save(book)
    }
}