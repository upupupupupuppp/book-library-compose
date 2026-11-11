package com.library.bookapi;

import com.library.bookapi.model.Book;
import com.library.bookapi.repository.BookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class BookApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApiApplication.class, args);
    }
}

@Component
class DatabaseInitializer {

    private final BookRepository bookRepository;

    DatabaseInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void waitForDatabase() {
        int attempts = 0;
        while (true) {
            try {
                bookRepository.count();
                System.out.println("Database is ready");
                break;
            } catch (Exception e) {
                if (++attempts >= 15) {
                    throw new RuntimeException("Could not connect to database after 15 attempts", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }
    }
}
