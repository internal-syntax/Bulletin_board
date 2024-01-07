package ru.skypro.homework.exceptions;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException() {
        super("Не верное изображение");
    }

    public InvalidImageException(String message) {
        super(message);
    }
}
