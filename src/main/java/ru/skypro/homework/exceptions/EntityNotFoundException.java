package ru.skypro.homework.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super("Сущность не найдена");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
