package com.cledson_leite.library_api.service;

import com.cledson_leite.library_api.model.BookRepository;
import com.cledson_leite.library_api.model.entity.Book;

public class BookService implements BookServiceInterface{
    private BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {
        return repository.save(book);
    }
}
