package com.cledson_leite.library_api.service;

import com.cledson_leite.library_api.model.BookRepository;
import com.cledson_leite.library_api.model.entity.Book;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookServiceInterface sut;
    @MockitoBean
    BookRepository repository;
    @BeforeEach
    public void setup(){
        sut =  new BookService(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = Book.builder().title("qualquer").author("qualquer").isbn("1234").build();
        Book savedBook = Book.builder().id(1l).title("qualquer").author("qualquer").isbn("1234").build();
        Mockito.when(repository.save(book)).thenReturn(savedBook);
        Book result = sut.save(book);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("qualquer");
        assertThat(result.getAuthor()).isEqualTo("qualquer");
        assertThat(result.getIsbn()).isEqualTo("1234");
    }
}
