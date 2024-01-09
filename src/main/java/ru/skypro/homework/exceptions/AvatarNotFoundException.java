package ru.skypro.homework.exceptions;

public class AvatarNotFoundException extends RuntimeException {
    public AvatarNotFoundException() {
        super("Аватар не найден");
    }

    public AvatarNotFoundException(String message) {
        super(message);
    }
}
