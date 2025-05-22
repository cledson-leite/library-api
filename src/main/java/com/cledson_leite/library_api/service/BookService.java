package com.cledson_leite.library_api.service;

import com.cledson_leite.library_api.exceptions.BusinessException;
import com.cledson_leite.library_api.model.BookRepository;
import com.cledson_leite.library_api.model.entity.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<Book> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Book> remove(long id) {
        Optional<Book> existedBook = repository.findById(id);
        existedBook.ifPresent( repository::delete);
         return existedBook;
    }

   public Optional<Book> updatedById(Book book) {
       Optional<Book> existedBook = repository.findById(book.getId());
       if(existedBook.isEmpty()){
           return Optional.empty();
       }
       Optional<Book> existedIsbn = repository.findByIsbn(book.getIsbn());
       if(existedIsbn.isPresent() && !existedIsbn.get().getId().equals(book.getId())) {
           throw new BusinessException("Isbn já cadastrado");
       }
       existedBook.get().setIsbn(book.getIsbn());
       existedBook.get().setTitle(book.getTitle());
       existedBook.get().setAuthor(book.getAuthor());
       Book updatedBook = repository.save(existedBook.get());
       return Optional.of(updatedBook);
   }
}
