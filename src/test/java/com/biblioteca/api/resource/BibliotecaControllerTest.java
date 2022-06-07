package com.biblioteca.api.resource;

import com.biblioteca.api.domain.Book;
import com.biblioteca.api.dto.BookDTO;
import com.biblioteca.service.BibliotecaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BibliotecaControllerTest {

    static String BOOK_API = "/api/books";
    @Autowired
    MockMvc mvc;
    @MockBean
    BibliotecaService bibliotecaService;

    @Test
    @DisplayName("Deve criar um livro")
    public void createBookTeste() throws Exception {

        BookDTO dto = createBook();
        Book savedBook = Book.builder().id(1l).author("joao").title("procastinaçao").isbn("124").build();
        BDDMockito.given(bibliotecaService.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1l))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
        ;
    }

    private BookDTO createBook() {
        return BookDTO.builder().author("joao").title("procastinaçao").isbn("124").build();
    }

    @Test
    @DisplayName("Deve obter informaçoes do livro")
    public void getBookDetails() throws Exception {
        Long id = 1l;
        Book book = Book.builder().id(id).title(createBook().getTitle()).author(createBook().getAuthor()).isbn(createBook().getIsbn()).build();
        BDDMockito.given(bibliotecaService.getById(id)).willReturn(Optional.of(book));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1l))
                .andExpect(jsonPath("title").value(createBook().getTitle()))
                .andExpect(jsonPath("author").value(createBook().getAuthor()))
                .andExpect(jsonPath("isbn").value(createBook().getIsbn()))
        ;
    }

    @Test
    @DisplayName("Deve retornar not found quando livro nao existir")
    public void bookNotFound() throws Exception {
        BDDMockito.given(bibliotecaService.getById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBook() throws Exception {
        Long id = 1l;
        String json = new ObjectMapper().writeValueAsString(createBook());
        Book updateBook = Book.builder().id(1l)
                .title("teste titulo").author("teste autor").isbn("12343").build();
        BDDMockito.given(bibliotecaService.getById(id)).willReturn(Optional.of(updateBook));
        BDDMockito.given(bibliotecaService.update(updateBook)).willReturn(Book.builder().id(id).author("joao").title("procastinaçao").isbn("124").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createBook().getTitle()))
                .andExpect(jsonPath("author").value(createBook().getAuthor()))
                .andExpect(jsonPath("isbn").value("124"));
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deletBook() throws Exception {
        BDDMockito.given(bibliotecaService.getById(anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1));

        mvc.perform(request)
                .andExpect(status().isNoContent());
    }
}