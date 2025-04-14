package ru.edme.exception;

public class EntityNotFoundException extends RuntimeException {

    // TODO: 11.04.2025 Забыл, обязательно ли тут своё поле!
    public EntityNotFoundException(String message) {
        super(message);
    }
}
