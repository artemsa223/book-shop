package com.example.demo;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookShopApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {

        SpringApplication.run(BookShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("The Catcher in the Rye");
            book.setAuthor("J.D. Salinger");
            book.setPrice(BigDecimal.TEN);
            book.setIsbn("1234-5678");
            book.setDescription("A novel about...");

            bookService.save(book);
            bookService.findAll().forEach(System.out::println);

        };
    }
}
