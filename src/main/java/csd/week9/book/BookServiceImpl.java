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
    // public int countLongestBookTitles(){
    //     List<Book> allBooks = books.findAll();
    //     List<Book> selectedBooks;
        
    //     int count = 0;
    //     int longestTitle = 0;
    //     for(Book book: allBooks){
    //         String s = book.getTitle();
    //         int charcount = s.length();
    //         // count words
    //         count = 0; boolean found = false; int eol = s.length() - 1;
    //         for (int i = 0; i < s.length(); i++) {
    //             if (!Character.isWhitespace(s.charAt(i)) && i != eol) {
    //                 found = true;
    //             } else if (Character.isWhitespace(s.charAt(i)) && found) {
    //                 count++;
    //                 found = false;
    //             } else if (!Character.isWhitespace(s.charAt(i)) && i == eol) {
    //                 count++;
    //             }
    //         }
    //         System.out.println(charcount);
    //         if (count > longestTitle)
    //             longestTitle = count;
            
    //     }
    //     int count1 = 0;
    //     for(Book book: allBooks){
    //         String s = book.getTitle();
    //         count = 0; boolean found = false; int eol = s.length() - 1;
    //         for (int i = 0; i < s.length(); i++) {
    //             if (Character.isLetter(s.charAt(i)) && i != eol) {
    //                 found = true;
    //             } else if (!Character.isLetter(s.charAt(i)) && found) {
    //                 count++;
    //                 found = false;
    //             } else if (Character.isLetter(s.charAt(i)) && i == eol) {
    //                 count++;
    //             }
    //         }
            
    //         if (count == longestTitle)
    //             count1++;
    //     }
        
    //     return count1;
    // }

    /**
     * Issues and Solution
        The code has almost no comments. Some comments are confusing -> Add comments
        From the Javadoc, it is not clear on how to determine the length of the title: using the count of words of chars -> Add comments
        The method itself is large, should be further divided -> Extract method (see point d below)
        A serious issue is code duplication: the part of code computing the word count for each title is repeated with slight modifications as shown in the snippets below -> Extract the word counting method.

        This part of the code can be considered feature-envy as well: counting the words in a book title should be a method in the Book class, not BookService -> Move method to the Book class.

        The below smells can be addressed by simplifying the word count method implementation.
        Reusing variables, e.g., count.
        Unused variables, e.g., selectedBooks; and variable for debugging is left behind, e.g., charcount.
        Variable names not clear at all, e.g., count, count1, eol, etc.
        Confusing logic in determining the word count; it could be implemented in much simpler ways
     * 
     */

    // Refactored codes
    
    /**
     * Get the length of the longest title among the provided book list
     * 
     * @param bookList a list of books
     * @return length of the longest title
     */
    public int getLongestTitleLength(List<Book> bookList) {
        int longestBookTitleLength = 0;
        for (Book book : bookList) {
            int tempWordCount = book.countWordsInTitle();
            if (tempWordCount > longestBookTitleLength) {
                longestBookTitleLength = tempWordCount;
            }
        }

        return longestBookTitleLength;
    }

    /**
     * Count the number of books whose titles are of length equal to the longestBookTitleLength
     * 
     * @param bookList a list of book
     * @param longestBookTitleLength matching word count
     * @return number of books
     */
    public int countBooks(List<Book> bookList, int longestBookTitleLength) {
        int counter = 0;
        for (Book book : bookList) {
            if (book.countWordsInTitle() == longestBookTitleLength) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Count the number of books having the longest title (in terms of word count)
     */
    public int countLongestBookTitles() {
        List<Book> bookList = books.findAll();
        int longestTitleLength = getLongestTitleLength(bookList);
        return countBooks(bookList, longestTitleLength);
    }
}