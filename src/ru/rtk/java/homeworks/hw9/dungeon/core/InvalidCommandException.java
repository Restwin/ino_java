package ru.rtk.java.homeworks.hw9.dungeon.core;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String m) {
        super(m);
    }
}