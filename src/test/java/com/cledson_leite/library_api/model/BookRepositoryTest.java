package com.cledson_leite.library_api.model;

import com.cledson_leite.library_api.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest //ativa e usa umdb em memoria para o jpa usar durante o teste
public class BookRepositoryTest {
    @Autowired
    TestEntityManager entityManager; // entidade  jpa para testes

    @Autowired
    BookRepository sut;

    @Test
    @DisplayName("Deve retornar verdadeiro caso existe um isbn já  cadastrado")
    public void returnTrue(){
        Book book = Book.builder().title("qualquer").author("qualquer").isbn("1234").build();
        entityManager.persist(book);
        boolean isExist = sut.existsByIsbn(book.getIsbn());
        assertThat(isExist).isTrue();
    }
    @Test
    @DisplayName("Deve retornar falso caso não existe um isbn já  cadastrado")
    public void returnFalse(){
        boolean isExist = sut.existsByIsbn("1234");
        assertThat(isExist).isFalse();
    }
}
