package com.company.exceptions;

public class CategoryAlredyExistsException extends RuntimeException{
    public CategoryAlredyExistsException(String message) {
        super(message);
    }
}
