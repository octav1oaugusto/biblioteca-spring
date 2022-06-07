package com.biblioteca.exceptions;

import com.biblioteca.api.domain.Book;

public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }
}
