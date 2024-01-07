package ru.skypro.homework.exceptions;

public class ForbiddenCommentException extends RuntimeException {
    public ForbiddenCommentException() {
        super("Не допустимый комментарий");
    }

    public ForbiddenCommentException(String message) {
        super(message);
    }
}
