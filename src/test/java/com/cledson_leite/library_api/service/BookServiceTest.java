package com.cledson_leite.library_api.service;

import com.cledson_leite.library_api.exceptions.BusinessException;
import com.cledson_leite.library_api.model.BookRepository;
import com.cledson_leite.library_api.model.entity.Book;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
        Book book = createNewBook();
        Book savedBook = Book.builder().id(1l).title("qualquer").author("qualquer").isbn("1234").build();
        Mockito.when(repository.save(book)).thenReturn(savedBook);
        Book result = sut.save(book);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("qualquer");
        assertThat(result.getAuthor()).isEqualTo("qualquer");
        assertThat(result.getIsbn()).isEqualTo("1234");
    }

    private static Book createNewBook() {
        return Book.builder().title("qualquer").author("qualquer").isbn("1234").build();
    }

    @Test
    @DisplayName("Deve lançar erro caso isbn seja duplicado")
    public void duplicatedIsbn(){
        Book book = createNewBook();
        Mockito.when(repository.existsByIsbn(book.getIsbn())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(() -> sut.save(book));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve retornar um livro pelo id")
    public void getByIdTest(){
        Long id = 10l;
        Book foundedBook = createNewBook();
        foundedBook.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(foundedBook));
        Optional<Book> result = sut.getById(id);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getTitle()).isEqualTo(foundedBook.getTitle());
        assertThat(result.get().getAuthor()).isEqualTo(foundedBook.getAuthor());
        assertThat(result.get().getIsbn()).isEqualTo(foundedBook.getIsbn());
    }
    @Test
    @DisplayName("Deve retornar um vazio caso o livro não exista")
    public void emptyReturn(){
        Long id = 10l;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Book> result = sut.getById(id);
        assertThat(result.isPresent()).isFalse();
    }
    @Test
    @DisplayName("Deve retornar o livro deletado em caso de sucesso")
    public void deleteByIdTest(){
        Long id = 10l;
        Book deletedBook = createNewBook();
        deletedBook.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(deletedBook));
        Optional<Book> result = sut.remove(id);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
    }
    @Test
    @DisplayName("Deve retornar um vazio caso o livro a ser deletado não exista")
    public void deleteEmptyReturn(){
        Long id = 10l;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Book> result = sut.remove(id);
        Mockito.verify(repository, Mockito.never()).delete(Mockito.any(Book.class));
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve atualizar um livro pelo id")
    public void updateBookTest(){
        Book book = createNewBook();
        book.setId(10l);
        Book existedBook = Book.builder().id(book.getId()).title("qualquer").author("qualquer").isbn("123").build();
        Mockito.when(repository.findById(book.getId())).thenReturn(Optional.of(existedBook));
        Mockito.when(repository.save(book)).thenReturn(book);
        Optional<Book> result = sut.updatedById(book);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(book.getId());
        assertThat(result.get().getTitle()).isEqualTo("qualquer");
        assertThat(result.get().getAuthor()).isEqualTo("qualquer");
        assertThat(result.get().getIsbn()).isEqualTo("1234");
    }

    @Test
    @DisplayName("Deve retornar um vazio caso o livro a ser atualizado não exista")
    public void updateEmptyReturn(){
        Book book = createNewBook();
        book.setId(10l);
        Mockito.when(repository.findById(book.getId())).thenReturn(Optional.empty());
        Optional<Book> result = sut.updatedById(book);
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Book.class));
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve lançar erro caso isbn autalizado já exista")
    public void duplicatedIsbnAtUpdate(){
        Book book = createNewBook();
        book.setId(10l);

        Book existedBook = Book.builder().id(book.getId()).title("qualquer").author("qualquer").isbn("123").build();
        Mockito.when(repository.findById(book.getId()))
                .thenReturn(Optional.of(existedBook));

        Mockito.when(repository.findByIsbn(book.getIsbn()))
                .thenReturn(Optional.of(Book.builder().id(11l).isbn(book.getIsbn()).build()));

        Throwable exception = Assertions.catchThrowable(() -> sut.updatedById(book));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");
        Mockito.verify(repository, Mockito.never()).save(book);
    }
}
