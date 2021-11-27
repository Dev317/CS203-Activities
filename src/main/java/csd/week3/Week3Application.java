package csd.week3;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import csd.week3.book.*;

@SpringBootApplication
public class Week3Application {

	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(Week3Application.class, args);

        // print out the book's info to the terminal
		BookController controller = ctx.getBean(BookController.class);
        List<Book> books = controller.getBooks();
        for(Book book : books){
            System.out.println(book.getTitle());
        }

        // TODO: Activity 3 - understand how to use RestTemplate
        // Uncomment the following code to observe and experiment with
        
        // Use BookClient which makes use of Spring's RestTemplate to consume the web service
        BookClient client = ctx.getBean(BookClient.class);

        // Our web service endpoint
        String URI = "http://localhost:8080/books";

        // Perform a GET request
        Book book = client.getBook(URI, 1L);
        System.out.println("[RestTemplate] GET book: " + book.getTitle());

        // Perform a POST request to add a new book
        Book newBook = new Book();
        newBook.setTitle("Gone With The Wind");
        Book returned = client.addBook(URI, newBook);
        System.out.println("[RestTemplate] POST book: " + returned.getTitle());
        
    }
    
}
