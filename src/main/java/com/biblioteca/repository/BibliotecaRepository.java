package com.biblioteca.repository;

import com.biblioteca.api.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliotecaRepository extends JpaRepository<Book,Long> {
    boolean existsByIsbn(String isbn);
}
