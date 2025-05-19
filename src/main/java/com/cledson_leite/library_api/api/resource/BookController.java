package com.cledson_leite.library_api.api.resource;

import com.cledson_leite.library_api.api.dto.BookDto;
import com.cledson_leite.library_api.model.entity.Book;
import com.cledson_leite.library_api.service.BookServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookServiceInterface service;
    private ModelMapper modelMapper; //lib que faz um mapper automaticamente

    public BookController(BookServiceInterface service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody BookDto dto){
        Book book = this.modelMapper.map(dto, Book.class);
        book.setId(10l);
        this.service.save(book);
        return book;
    }
}
