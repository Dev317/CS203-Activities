package csd.week11.book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;


@Service
public class BookServiceImpl implements BookService {
   
    private BookRepository books;
    

    public BookServiceImpl(BookRepository books){
        this.books = books;
    }

    @Override
    public List<Book> listBooks() {
        return books.findAll();
    }

    
    @Override
    public Book getBook(Long id){
        return books.findById(id).orElse(null);
    }
    
    /**
     * Add logic to avoid adding books with the same title
     * Return null if there exists a book with the same title
     */
    @Override
    public Book addBook(Book book) {
        List<Book> sameTitles = books.findByTitle(book.getTitle());
        if(sameTitles.size() == 0)
            return books.save(book);
        else
            return null;
    }
    
    @Override
    public Book updateBook(Long id, Book newBookInfo){
        return books.findById(id).map(book -> {book.setTitle(newBookInfo.getTitle());
            return books.save(book);
        }).orElse(null);

    }

    /**
     * Remove a book with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a book will also remove all its associated reviews
     */
    @Override
    public void deleteBook(Long id){
        books.deleteById(id);
    }
   
    /**
     * Activity 2 (Week 11): Pair-programming exercise
     * 
     * Return the k-most common words (case insensitive) in all book titles.
     * The words returned (in a map) are sorted based on their count, and then alphabetical order.
     * 
     * Example: if there are two books with titles "I'm a Book", and "This book is great"
     * Then getMostCommonWordsInTitles(2) will return a map with the following (key, value) pairs:
     * ("book", 2) and ("a", 1).
     * getMostCommonWordsInTitles(3) will return a map with the following (key, value) pairs:
     * ("book", 2), ("a", 1) and ("is", 1).
     * 
     * @return Map<String, Integer> a map of k-most common words and their correponding counts.
     */
    @Override
    public Map<String, Integer> getMostCommonWordsInTitles(int topK){
        Map<String, Integer> returnedMap = new LinkedHashMap<String, Integer>();
        // your code here
        
        return returnedMap;
    }
}
