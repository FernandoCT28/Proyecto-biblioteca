package com.api.biblioteca.service.books;

import com.api.biblioteca.dto.books.BookDto;
import com.api.biblioteca.dto.clients.ClientDto;

import java.util.List;
import java.util.Optional;


public interface BookService {

    BookDto saveBook(BookDto book);

    List<BookDto> getAllBooks();

    BookDto updateBook(Long id, BookDto bookDto, Long clientId);

    void deleteBookById(Long id);

    Optional<BookDto> getBookById(Long id);
}
