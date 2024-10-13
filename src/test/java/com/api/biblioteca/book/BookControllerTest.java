package com.api.biblioteca.book;

import com.api.biblioteca.controller.books.BookController;
import com.api.biblioteca.dto.books.BookDto;
import com.api.biblioteca.exception.ErrorResponse;
import com.api.biblioteca.service.books.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooksSuccess() {
        List<BookDto> books = Arrays.asList(new BookDto(1L, "Title1", "Author1", "Editorial1", "ISBN1", "2021-08-01", 10.0, "Available", null));
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BookDto>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetAllBooksFailure() {
        when(bookService.getAllBooks()).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<List<BookDto>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testCreateBookSuccess() {
        BookDto bookDto = new BookDto(1L, "Title1", "Author1", "Editorial1", "ISBN1", "2021-08-01", 10.0, "Available", null);
        when(bookService.saveBook(bookDto)).thenReturn(bookDto);

        ResponseEntity<BookDto> response = bookController.createBook(bookDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDto, response.getBody());
        verify(bookService, times(1)).saveBook(bookDto);
    }

    @Test
    void testCreateBookIllegalArgument() {
        BookDto bookDto = new BookDto(null, null, null, null, null, null, null, null, "Invalid data");
        when(bookService.saveBook(bookDto)).thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<BookDto> response = bookController.createBook(bookDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody().getClientName());
        verify(bookService, times(1)).saveBook(bookDto);
    }

    @Test
    void testUpdateBookSuccess() {
        Long bookId = 1L;
        BookDto bookDto = new BookDto(bookId, "Title1", "Author1", "Editorial1", "ISBN1", "2021-08-01", 10.0, "Available", null);
        when(bookService.updateBook(eq(bookId), any(BookDto.class), isNull())).thenReturn(bookDto);

        ResponseEntity<BookDto> response = bookController.updateBook(bookId, bookDto, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDto, response.getBody());
        verify(bookService, times(1)).updateBook(eq(bookId), any(BookDto.class), isNull());
    }

    @Test
    void testGetBookByIdNotFound() {
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Client not found", ((ErrorResponse) response.getBody()).getMessage());
        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void testDeleteBookByIdSuccess() {
        Long bookId = 1L;

        ResponseEntity<?> response = bookController.deleteBookById(bookId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBookById(bookId);
    }

    @Test
    void testDeleteBookByIdNotFound() {
        Long bookId = 1L;
        doThrow(new RuntimeException("Failed to delete client with ID: " + bookId)).when(bookService).deleteBookById(bookId);

        ResponseEntity<?> response = bookController.deleteBookById(bookId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(bookService, times(1)).deleteBookById(bookId);
    }

}
