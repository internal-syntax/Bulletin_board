package ru.skypro.homework.exceptions;

public class InvalidCommentException extends RuntimeException {
    public InvalidCommentException() {
        super("Не верный комментарий");
    }

    public InvalidCommentException(String message) {
        super(message);
    }
}
