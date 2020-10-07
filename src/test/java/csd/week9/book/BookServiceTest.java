package csd.week9.book;

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

import csd.week9.book.Book;
import csd.week9.book.BookRepository;
import csd.week9.book.BookServiceImpl;

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
    void countLongestTitles_OneLongest_Return1(){
        // your code here
        List<Book> newBooks = new ArrayList<Book>();
        newBooks.add(new Book("The Longest"));
        newBooks.add(new Book("The Longest Title"));
        newBooks.add(new Book("The Longest Title of Book"));
        when(books.findAll()).thenReturn(newBooks);

        int count = bookService.countLongestBookTitles();
        
        assertEquals(1, count);
        verify(books).findAll();
    }

    @Test
    void countLongestTitles_TwoLongest_Return2(){
        // your code here
        List<Book> newBooks = new ArrayList<Book>();
        newBooks.add(new Book("The Longest"));
        newBooks.add(new Book("The Longer Title of Book"));
        newBooks.add(new Book("The Longest"));
        newBooks.add(new Book("The Longest Title of Book"));
        
        when(books.findAll()).thenReturn(newBooks);

        int count = bookService.countLongestBookTitles();
        
        assertEquals(2, count);
        verify(books).findAll();
    }

    @Test
    void countLongestTitles_AllLongest_Return4(){
        // your code here
        List<Book> newBooks = new ArrayList<Book>();
        newBooks.add(new Book("The Long Book"));
        newBooks.add(new Book("The Longer Book"));
        newBooks.add(new Book("The Longerer Book"));
        newBooks.add(new Book("The Longest Book"));
        
        when(books.findAll()).thenReturn(newBooks);

        int count = bookService.countLongestBookTitles();
        
        assertEquals(4, count);
        verify(books).findAll();
    }
   
}