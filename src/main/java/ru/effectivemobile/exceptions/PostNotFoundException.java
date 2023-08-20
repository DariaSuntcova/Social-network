package ru.effectivemobile.exceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException() {
        super("Поста с таким id не существует");
    }
}
