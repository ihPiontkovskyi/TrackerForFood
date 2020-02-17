package ua.foodtracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.foodtracker.exception.NotFoundException;
import ua.foodtracker.exception.ServerError;

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

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound() {
        return "error/error-page";
    }

    @ExceptionHandler(ServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServerError() {
        return "error/internal-error";
    }
}
