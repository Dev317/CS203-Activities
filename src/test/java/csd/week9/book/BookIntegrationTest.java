package csd.week9.book;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import csd.week9.book.Book;
import csd.week9.book.BookRepository;
import csd.week9.user.User;
import csd.week9.user.UserRepository;

/** Start an actual HTTP server listening at a random port*/
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BookIntegrationTest {

	@LocalServerPort
	private int port;

	private final String baseUrl = "http://localhost:";

	@Autowired
	/**
	 * Use TestRestTemplate for testing a real instance of your application as an external actor.
	 * Convenient subclass of RestTemplate that is suitable for integration tests.
 	 * It is fault tolerant, and optionally can carry Basic authentication headers.
	 */
	private TestRestTemplate restTemplate;

	@Autowired
	private BookRepository books;

	@Autowired
	private UserRepository users;

	@Autowired
	private BCryptPasswordEncoder encoder;


	@AfterEach
	void tearDown(){
		books.deleteAll();
		users.deleteAll();
	}

	@Test
	public void getBooks_Success() throws Exception {
		URI uri = new URI(baseUrl + port + "/books");
		books.save(new Book("Gone With The Wind"));
		
		// Need to use array with a ReponseEntity here
		ResponseEntity<Book[]> result = restTemplate.getForEntity(uri, Book[].class);
		Book[] books = result.getBody();
		
		assertEquals(200, result.getStatusCode().value());
		assertEquals(1, books.length);
	}

	@Test
	public void getBook_ValidBookId_Success() throws Exception {
		Book book = new Book("Gone With The Wind");
		Long id = books.save(book).getId();
		URI uri = new URI(baseUrl + port + "/books/" + id);
		
		ResponseEntity<Book> result = restTemplate.getForEntity(uri, Book.class);
			
		assertEquals(200, result.getStatusCode().value());
		assertEquals(book.getTitle(), result.getBody().getTitle());
	}

	@Test
	public void getBook_InvalidBookId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/books/1");
		
		ResponseEntity<Book> result = restTemplate.getForEntity(uri, Book.class);
			
		assertEquals(404, result.getStatusCode().value());
	}

	@Test
	public void addBook_Success() throws Exception {
		URI uri = new URI(baseUrl + port + "/books");
		Book book = new Book("A New Hope");
		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

		ResponseEntity<Book> result = restTemplate.withBasicAuth("admin", "goodpassword")
										.postForEntity(uri, book, Book.class);
			
		assertEquals(201, result.getStatusCode().value());
		assertEquals(book.getTitle(), result.getBody().getTitle());
	}

	/**
	 * Integration tests for delete/update a book.
	 * For delete operation: there should be two tests for success and failure scenarios.
	 * Similarly, there should be two tests for update operation.
	 */
	@Test
	public void deleteBook_ValidBookId_Success() throws Exception {
		Book book = books.save(new Book("A New Hope"));
		URI uri = new URI(baseUrl + port + "/books/" + book.getId().longValue());
		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
		
		//restTemplate.withBasicAuth("admin", "goodpassword").delete(uri);
		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
										.exchange(uri, HttpMethod.DELETE, null, Void.class);
		
		assertEquals(200, result.getStatusCode().value());
		// An empty Optional should be returned by "findById" after deletion
		Optional<Book> emptyValue = Optional.empty();
		assertEquals(emptyValue, books.findById(book.getId()));
	}

	@Test
	public void deleteBook_InvalidBookId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/books/1");
		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
		
		ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
										.exchange(uri, HttpMethod.DELETE, null, Void.class);
		
		assertEquals(404, result.getStatusCode().value());
	}

	@Test
	public void updateBook_ValidBookId_Success() throws Exception {
		Book book = books.save(new Book("A New Hope"));
		URI uri = new URI(baseUrl + port + "/books/" + book.getId().longValue());
		Book newBookInfo = new Book("A New Vision");
		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
		
		ResponseEntity<Book> result = restTemplate.withBasicAuth("admin", "goodpassword")
										.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newBookInfo), Book.class);
			
		assertEquals(200, result.getStatusCode().value());
		assertEquals(newBookInfo.getTitle(), result.getBody().getTitle());
	}

	@Test
	public void updateBook_InvalidBookId_Failure() throws Exception {
		URI uri = new URI(baseUrl + port + "/books/1");
		Book newBookInfo = new Book("A New Vision");
		users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));
		
		ResponseEntity<Book> result = restTemplate.withBasicAuth("admin", "goodpassword")
										.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newBookInfo), Book.class);
			
		assertEquals(404, result.getStatusCode().value());
	}

}
