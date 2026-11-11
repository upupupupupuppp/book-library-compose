package com.library.bookapi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String author;

    @Column(nullable = false, unique = true, length = 50)
    private String isbn;

    private Integer year;

    @Column(length = 100)
    private String genre;

    private Boolean available = true;

    @Column(name = "created_at_utc")
    private Instant createdAtUtc = Instant.now();

    public Book() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public Instant getCreatedAtUtc() { return createdAtUtc; }
    public void setCreatedAtUtc(Instant createdAtUtc) { this.createdAtUtc = createdAtUtc; }
}
