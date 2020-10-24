package csd.week11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import csd.week11.book.*;
import csd.week11.user.User;
import csd.week11.user.UserRepository;

@SpringBootApplication
public class Week11Application {

	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(Week11Application.class, args);

        // JPA book repository init
        BookRepository books = ctx.getBean(BookRepository.class);
        System.out.println("[Add book]: " + books.save(new Book("Spring Security Fundamentals")).getTitle());
        System.out.println("[Add book]: " + books.save(new Book("Gone With The Wind")).getTitle());

        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN")).getUsername());
        
    }
    
}

    