package com.biblioteca.repository;

import com.biblioteca.api.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BibliotecaRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BibliotecaRepository bibliotecaRepository;


    @Test
    @DisplayName("Deve retornar verdadeiro quando houver o isbn informado")
    public void verificaIsbn(){
        String isbn = "124";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        boolean exists = bibliotecaRepository.existsByIsbn(isbn);

        Assertions.assertThat(exists).isTrue();
    }

    private Book createNewBook(String isbn){
        return Book.builder().title("teste").author("teste").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findById(){
        Book book = createNewBook("123");
        entityManager.persist(book);

        Optional<Book> founBook =  bibliotecaRepository.findById(book.getId());

        Assertions.assertThat(founBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBook(){
        Book book = createNewBook("123");

        Book savedBook = bibliotecaRepository.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um book")
    public void deletBook(){
        Book book = createNewBook("123");
        entityManager.persist(book);

        Book fundBook = entityManager.find(Book.class,book.getId());
        bibliotecaRepository.delete(fundBook);

        Book deletBook = entityManager.find(Book.class, book.getId());
        Assertions.assertThat(deletBook).isNull();
    }
}
