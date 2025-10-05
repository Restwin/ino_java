package ru.rtk.java.homeworks.hw9.dungeon.model;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    private Player player;
    private Room current;
    private int score;
    private final Map<String, Room> allRooms = new HashMap<>();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public Room getCurrent() {
        return current;
    }

    public void setCurrent(Room r) {
        this.current = r;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int d) {
        this.score += d;
    }

    public Map<String, Room> getAllRooms() {
        return allRooms;
    }

    public void resetWorld(Map<String, Room> newWorld) {
        allRooms.clear();
        allRooms.putAll(newWorld);
    }
}