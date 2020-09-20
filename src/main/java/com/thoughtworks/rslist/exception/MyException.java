package com.thoughtworks.rslist.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyException {

    Logger logger = LoggerFactory.getLogger(MyException.class);

    @ExceptionHandler({MyIndexOutOfBoundsException.class, IndexException.class, InvaildRsEventExcepttion.class,InvalidUserException.class})
    public ResponseEntity handleIndexOutOfBoundsException(Exception ex) {
        logger.error(ex.getMessage());
        CommentError commentError = new CommentError(ex.getMessage());
        return ResponseEntity.badRequest().body(commentError);
    }
}
