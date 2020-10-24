package csd.week11.book;

import java.util.List;
import java.util.Map;

public interface BookService {
    List<Book> listBooks();
    Book getBook(Long id);
    Book addBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);

    Map<String, Integer> getMostCommonWordsInTitles(int topK);
}