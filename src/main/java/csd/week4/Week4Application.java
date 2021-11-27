package csd.week4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import csd.week4.book.*;

@SpringBootApplication
public class Week4Application {

	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);

        // JPA repository init
        BookRepository jpaRepo = ctx.getBean(BookRepository.class);
        System.out.println(jpaRepo.save(new Book("JPA Fundamentals")).getTitle());
        System.out.println(jpaRepo.save(new Book("Gone With The Wind")).getTitle());
    }
    
}
