package com.cledson_leite.library_api.api.resource;

import com.cledson_leite.library_api.api.dto.BookDto;
import com.cledson_leite.library_api.exceptions.BusinessException;
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


import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
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
        BookDto dto = createNewBook();
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

    private static BookDto createNewBook() {
        return BookDto.builder().title("As aventuras").author("Artur").isbn("001").build();
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(BookDto.builder().build());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro caso isbn seja duplicado")
    public void duplicatedIsbnError() throws Exception {
        BookDto dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String error = "Isbn já cadastrado";
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(error));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(error));
    }

    @Test
    @DisplayName("Deve retornar uma livro por id")
    public void getBookById() throws Exception{
        Long id = 10l;
        BookDto dto = createNewBook();
        Book savedBook = Book.builder().id(id).title(dto.getTitle()).author(dto.getAuthor()).isbn(dto.getIsbn()).build();
        BDDMockito.given(service.getById(Mockito.any())).willReturn(Optional.of(savedBook));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }
    @Test
    @DisplayName("Deve lançar notFaund error caso não existe um livro com esse id")
    public void notFoundById() throws Exception{
        Long id = 10l;
        BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Deve remover um livro pelo id")
    public void deleteById() throws Exception{
        Long id = 10l;
        BDDMockito.given(service.remove(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(id).build()));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/"+id));
        mvc.perform(request)
                .andExpect(status().isNoContent());
    }
    @Test
    @DisplayName("Deve lançar notFaund error  ao deletar um livro inexistente")
    public void deleteNotFound() throws Exception{
        Long id = 10l;
        BDDMockito.given(service.remove(Mockito.anyLong())).willReturn(Optional.empty());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/"+id));
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar uma livro com sucesso")
    public void updateByIdTest() throws Exception{
        Long id = 10l;
        BookDto dto = createNewBook();
        Book updatedBook = Book.builder().id(id).title(dto.getTitle()).author(dto.getAuthor()).isbn(dto.getIsbn()).build();
        BDDMockito.given(service.updatedById(Mockito.any(Book.class))).willReturn(Optional.of(updatedBook));
        String json = new ObjectMapper().writeValueAsString(dto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }
    @Test
    @DisplayName("Deve lançar notFaund error  ao atualizar um livro inexistente")
    public void updateNotFound() throws Exception{
        Long id = 10l;
        BookDto dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given(service.updatedById(Mockito.any())).willReturn(Optional.empty());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve lançar erro caso atualize com um isbn duplicado")
    public void updatedDuplicatedIsbnError() throws Exception {
        Long id = 10l;
        BookDto dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String error = "Isbn já cadastrado";
        BDDMockito.given(service.updatedById(Mockito.any(Book.class)))
                .willThrow(new BusinessException(error));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(error));
    }
}
