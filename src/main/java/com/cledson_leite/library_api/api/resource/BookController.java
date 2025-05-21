package com.cledson_leite.library_api.api.resource;

import com.cledson_leite.library_api.api.dto.BookDto;
import com.cledson_leite.library_api.api.exceptions.AppErrors;
import com.cledson_leite.library_api.exceptions.BusinessException;
import com.cledson_leite.library_api.model.entity.Book;
import com.cledson_leite.library_api.service.BookServiceInterface;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public Book create(@RequestBody @Valid BookDto dto){
        Book book = this.modelMapper.map(dto, Book.class);
        book.setId(10l);
        this.service.save(book);
        return book;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppErrors handleValidationsExceptions(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        return new AppErrors(bindingResult);
    }
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppErrors handleBusinessExceptions(BusinessException exception){
        return new AppErrors(exception);
    }
}
