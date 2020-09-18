package com.thoughtworks.rslist.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyException {
    @ExceptionHandler({MyIndexOutOfBoundsException.class, IndexException.class, InvaildRsEventExcepttion.class,InvalidUserException.class})
    public ResponseEntity handleIndexOutOfBoundsException(Exception ex) {
        CommentError commentError = new CommentError(ex.getMessage());
        return ResponseEntity.badRequest().body(commentError);
    }
}
