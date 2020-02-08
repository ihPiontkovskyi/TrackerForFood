package ua.foodtracker.service.exception;

public class IncorrectDataException extends RuntimeException {
    public IncorrectDataException(String message) {
        super(message);
    }

    public IncorrectDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
