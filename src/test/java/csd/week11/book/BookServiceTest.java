package csd.week11.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @Mock
    private BookRepository books;

    @InjectMocks
    private BookServiceImpl bookService;
    
    
    @Test
    void addBook_NewTitle_ReturnSavedBook(){
        // arrange ***
        Book book = new Book("This is a New Title");
        // mock the "findbytitle" operation
        when(books.findByTitle(any(String.class))).thenReturn(new ArrayList<Book>());
        // mock the "save" operation 
        when(books.save(any(Book.class))).thenReturn(book);

        // act ***
        Book savedBook = bookService.addBook(book);
        
        // assert ***
        assertNotNull(savedBook);
        verify(books).findByTitle(book.getTitle());
        verify(books).save(book);
    }
    /**
     * Write a test case for adding a new book but the title already exists
     * The test case should pass if BookServiceImpl.addBook(book)
     * returns null (can't add book), otherwise it will fail.
     * Remember to include suitable "verify" operations
     * 
     */
    @Test
    void addBook_SameTitle_ReturnNull(){
        // your code here
        Book book = new Book("The Same Title Exists");
        List<Book> sameTitles = new ArrayList<Book>();
        sameTitles.add(new Book("The Same Title Exists"));
        when(books.findByTitle(book.getTitle())).thenReturn(sameTitles);

        Book savedBook = bookService.addBook(book);
        
        assertNull(savedBook);
        verify(books).findByTitle(book.getTitle());
    }

    @Test
    void updateBook_NotFound_ReturnNull(){
        Book book = new Book("Updated Title of Book");
        Long bookId = 10L;
        when(books.findById(bookId)).thenReturn(Optional.empty());
        
        Book updatedBook = bookService.updateBook(bookId, book);
        
        assertNull(updatedBook);
        verify(books).findById(bookId);
    }

    @Test
    void updateBook_Found_ReturnBook(){
        Book current = new Book("Title of Book");
        Book update = new Book("Updated Title of Book");
        Long bookId = 10L;
        when(books.findById(bookId)).thenReturn(Optional.of(current));
        when(books.save(any(Book.class))).thenReturn(update);
        
        Book updatedBook = bookService.updateBook(bookId, update);
        
        assertEquals(update.getTitle(), updatedBook.getTitle());
        verify(books).findById(bookId);
    }

    @Test
    void getMostCommonWordsInTitles_ReturnEmptyMap(){
        Map<String, Integer> expected = new LinkedHashMap<String, Integer>();
        
        Map<String, Integer> actual = bookService.getMostCommonWordsInTitles(0);
        
        assertTrue(actual.equals(expected));
    }

    @Test
    void getMostCommonWordsInTitles_SortByWordCount(){
        List<Book> newBooks = new ArrayList<Book>();
        newBooks.add(new Book("Book book book"));
        newBooks.add(new Book("Title title"));
        newBooks.add(new Book("Word word word word"));
        when(books.findAll()).thenReturn(newBooks);
        Map<String, Integer> expected = new LinkedHashMap<String, Integer>();
        expected.put("book", 3);
        expected.put("word", 4);
        
        Map<String, Integer> actual = bookService.getMostCommonWordsInTitles(2);
        
        assertTrue(actual.equals(expected));
        verify(books).findAll();
    }
}