package ru.rtk.java.homeworks.hw9.dungeon.core;

import ru.rtk.java.homeworks.hw9.dungeon.model.GameState;
import java.util.List;

@FunctionalInterface
public interface Command { void execute(GameState ctx, List<String> args); }