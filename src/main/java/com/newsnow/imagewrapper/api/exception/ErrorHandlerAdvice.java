package com.newsnow.imagewrapper.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandlerAdvice{

        public static class ErrorMessage{
                private final String message;


                private ErrorMessage(String message) {
                        this.message = message;
                }
                static ErrorMessage of(String message){
                        return new ErrorMessage(message);
                }

                public String getMessage() {
                        return message;
                }
        }

        @ExceptionHandler(ValidationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        ResponseEntity<ErrorMessage> onConstraintValidationException(
                ValidationException e) {

            return ResponseEntity.badRequest().body(ErrorMessage.of(e.getMessage()));
        }


}