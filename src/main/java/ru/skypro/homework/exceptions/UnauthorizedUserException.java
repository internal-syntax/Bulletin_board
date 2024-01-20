package ru.skypro.homework.exceptions;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super("Пользователь не авторизован для выполнения этой операции");
    }

    public UnauthorizedUserException(String message) {
        super(message);
    }
}
