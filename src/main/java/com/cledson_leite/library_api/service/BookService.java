package com.cledson_leite.library_api.service;

import com.cledson_leite.library_api.exceptions.BusinessException;
import com.cledson_leite.library_api.model.BookRepository;
import com.cledson_leite.library_api.model.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService implements BookServiceInterface{

    private BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado");
        }
        return repository.save(book);
    }
}
