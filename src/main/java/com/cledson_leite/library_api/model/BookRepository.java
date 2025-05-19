package com.cledson_leite.library_api.model;

import com.cledson_leite.library_api.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
