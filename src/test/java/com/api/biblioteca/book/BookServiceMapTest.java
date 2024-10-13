package com.api.biblioteca.book;

import com.api.biblioteca.dto.books.BookDto;
import com.api.biblioteca.entity.BookPostEntity;
import com.api.biblioteca.entity.ClientPostEntity;
import com.api.biblioteca.exception.UserNotFoundException;
import com.api.biblioteca.repository.BooksPostRepository;
import com.api.biblioteca.repository.ClientPostRepository;
import com.api.biblioteca.service.books.BookServiceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceMapTest {

    @Mock
    private BooksPostRepository bookRepository;

    @Mock
    private ClientPostRepository clientRepository;

    @InjectMocks
    private BookServiceMap bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBookSuccess() {
        BookDto bookDto = new BookDto(1L, "Title1", "Author1", "Editorial1", "ISBN1", "2021-08-01", 10.0, "Available", null);
        BookPostEntity bookEntity = new BookPostEntity();
        bookEntity.setId(1L);
        when(bookRepository.save(any(BookPostEntity.class))).thenReturn(bookEntity);

        BookDto result = bookService.saveBook(bookDto);

        assertNotNull(result);
        assertEquals(bookEntity.getId(), result.getId());
        verify(bookRepository, times(1)).save(any(BookPostEntity.class));
    }

    @Test
    void testUpdateBookSuccess() {
        Long bookId = 1L;
        BookDto bookDto = new BookDto(bookId, "Title1", "Author1", "Editorial1", "ISBN1", "2021-08-01", 10.0, "Available", null);
        BookPostEntity bookEntity = new BookPostEntity();
        bookEntity.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);

        BookDto result = bookService.updateBook(bookId, bookDto, null);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(bookEntity);
    }

    @Test
    void testUpdateBookNotFound() {
        Long bookId = 1L;
        BookDto bookDto = new BookDto(bookId, "Title1", "Author1", "Editorial1", "ISBN1", "2021-08-01", 10.0, "Available", null);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(bookId, bookDto, null));
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(0)).save(any(BookPostEntity.class));
    }

    @Test
    void testDeleteBookByIdSuccess() {
        Long bookId = 1L;
        BookPostEntity bookEntity = new BookPostEntity();
        bookEntity.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

        assertDoesNotThrow(() -> bookService.deleteBookById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(bookEntity);
    }


    @Test
    void testDeleteBookByIdNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookService.deleteBookById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(0)).deleteById(bookId);
    }

    @Test
    void testFindByIdSuccess() {
        Long bookId = 1L;
        BookPostEntity bookEntity = new BookPostEntity();
        bookEntity.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

        Optional<BookDto> result = bookService.getBookById(bookId);

        assertTrue(result.isPresent());
        assertEquals(bookId, result.get().getId());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testFindByIdNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookService.getBookById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

}
