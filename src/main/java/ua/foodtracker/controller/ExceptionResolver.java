package ua.foodtracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "error/error-page";
    }

    @ExceptionHandler(Error.class)
    public String handleError() {
        return "error/internal-error";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Throwable.class)
    public String handleNotFound() {
        return "error/error-page";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public String handleServerError() {
        return "error/internal-error";
    }
}
