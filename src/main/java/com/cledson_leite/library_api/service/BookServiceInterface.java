package com.cledson_leite.library_api.service;

import com.cledson_leite.library_api.api.dto.BookDto;
import com.cledson_leite.library_api.model.entity.Book;

import java.util.Optional;

public interface BookServiceInterface {
    Book save(Book any);

    Optional<Book >getById(Long id);

    Optional<Book> remove(long l);

    Optional<Book> updatedById(Book any);
}

