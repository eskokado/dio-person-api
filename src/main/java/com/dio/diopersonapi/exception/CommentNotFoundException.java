package com.dio.diopersonapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends Exception {
    public CommentNotFoundException(Long id) {
        super(String.format("Comment with ID %s not found!", id));
    }
}
