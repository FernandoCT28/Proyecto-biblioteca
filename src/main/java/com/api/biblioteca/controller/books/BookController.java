package com.api.biblioteca.controller.books;

import com.api.biblioteca.dto.books.BookDto;
import com.api.biblioteca.dto.clients.ClientDto;
import com.api.biblioteca.exception.ErrorResponse;
import com.api.biblioteca.exception.UserNotFoundException;
import com.api.biblioteca.service.books.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        try {
            List<BookDto> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        try {
            BookDto savedBook = bookService.saveBook(bookDto);
            return ResponseEntity.ok(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BookDto(null, null, null, null, null, null, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BookDto(null, null, null, null, null, null, null, null, "Internal server error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto, @RequestParam(required = false) Long clientId) {
        try {
            BookDto updatedBook = bookService.updateBook(id, bookDto, clientId);
            return ResponseEntity.ok(updatedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BookDto(null, null, null, null, null, null, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BookDto(null, null, null, null, null, null, null, null, "Internal server error"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            BookDto bookDto = bookService.getBookById(id).orElse(null);
            if (bookDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Client not found"));
            }
            return ResponseEntity.ok(bookDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        try {
            bookService.deleteBookById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }



}
