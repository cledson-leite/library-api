package com.cledson_leite.library_api.api.resource;

import com.cledson_leite.library_api.api.dto.BookDto;
import com.cledson_leite.library_api.model.entity.Book;
import com.cledson_leite.library_api.service.BookServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {
    static String BOOK_API = "/api/books";
    @Autowired
    MockMvc mvc;

    @MockitoBean
    BookServiceInterface service;

    @Test
    @DisplayName("Deve criar uma livro com sucesso")
    public void createBookTest() throws Exception{ // esta lançando para frente o erro
        BookDto dto = BookDto.builder().title("As aventuras").author("Artur").isbn("001").build();
        Book savedBook = Book.builder().id(10l).title(dto.getTitle()).author(dto.getAuthor()).isbn(dto.getIsbn()).build();
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto); //tranforma qualquer objeto em json
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API) //metodo http recebe uma string com a url
                .contentType(MediaType.APPLICATION_JSON) //tipo conteudo
                .accept(MediaType.APPLICATION_JSON) // tipos aceitaveis
                .content(json);// body da requisição
        mvc.perform(request) // é a ação recebendo qual a rquesição será feita
                .andExpect(status().isCreated()) //assertiva esperada espera que  o estatus seja 201
                .andExpect(jsonPath("id").isNotEmpty()) // espera que o json tenha um id e não seja vazio
                .andExpect(jsonPath("title").value(dto.getTitle())) // espera que o json tenha um titulo e  seja "qualquer"
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }
    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação")
    public void createInvalidBookTest(){}
}
