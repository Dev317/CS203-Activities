package csd.week9.book;

import java.util.List;
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
     * TODO: Activity 1 - Review the code in this method and highlight any issues.
     * 
     * Count the books with longest titles.
     * 
     */
    public int countLongestBookTitles(){
        List<Book> allBooks = books.findAll();
        List<Book> selectedBooks;
        
        int count = 0;
        int longestTitle = 0;
        for(Book book: allBooks){
            String s = book.getTitle();
            int charcount = s.length();
            // count words
            count = 0; boolean found = false; int eol = s.length() - 1;
            for (int i = 0; i < s.length(); i++) {
                if (!Character.isWhitespace(s.charAt(i)) && i != eol) {
                    found = true;
                } else if (Character.isWhitespace(s.charAt(i)) && found) {
                    count++;
                    found = false;
                } else if (!Character.isWhitespace(s.charAt(i)) && i == eol) {
                    count++;
                }
            }
            System.out.println(charcount);
            if (count > longestTitle)
                longestTitle = count;
            
        }
        int count1 = 0;
        for(Book book: allBooks){
            String s = book.getTitle();
            count = 0; boolean found = false; int eol = s.length() - 1;
            for (int i = 0; i < s.length(); i++) {
                if (Character.isLetter(s.charAt(i)) && i != eol) {
                    found = true;
                } else if (!Character.isLetter(s.charAt(i)) && found) {
                    count++;
                    found = false;
                } else if (Character.isLetter(s.charAt(i)) && i == eol) {
                    count++;
                }
            }
            
            if (count == longestTitle)
                count1++;
        }
        
        return count1;
    }
}