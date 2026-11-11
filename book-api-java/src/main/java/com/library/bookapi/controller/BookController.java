package com.library.bookapi.controller;

import com.library.bookapi.model.Book;
import com.library.bookapi.model.BookInput;
import com.library.bookapi.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of("service", "Book Library API", "status", "running");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        try {
            bookRepository.count();
            return ResponseEntity.ok(Map.of("api", "ok", "database", "ok"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Database connection failed"));
        }
    }

    @GetMapping("/books")
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/books")
    public ResponseEntity<?> create(@RequestBody BookInput input) {
        if (input.getTitle() == null || input.getTitle().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Title is required"));
        }
        if (input.getAuthor() == null || input.getAuthor().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Author is required"));
        }
        if (input.getIsbn() == null || input.getIsbn().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "ISBN is required"));
        }

        Book book = new Book();
        book.setTitle(input.getTitle().trim());
        book.setAuthor(input.getAuthor().trim());
        book.setIsbn(input.getIsbn().trim());
        book.setYear(input.getYear());
        book.setGenre(input.getGenre() != null ? input.getGenre().trim() : null);
        book.setAvailable(input.getAvailable() != null ? input.getAvailable() : true);

        Book saved = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BookInput input) {
        if (input.getTitle() == null || input.getTitle().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Title is required"));
        }
        if (input.getAuthor() == null || input.getAuthor().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Author is required"));
        }
        if (input.getIsbn() == null || input.getIsbn().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "ISBN is required"));
        }

        return bookRepository.findById(id).map(book -> {
            book.setTitle(input.getTitle().trim());
            book.setAuthor(input.getAuthor().trim());
            book.setIsbn(input.getIsbn().trim());
            book.setYear(input.getYear());
            book.setGenre(input.getGenre() != null ? input.getGenre().trim() : null);
            if (input.getAvailable() != null) {
                book.setAvailable(input.getAvailable());
            }
            return ResponseEntity.ok(bookRepository.save(book));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
