package ru.rtk.java.homeworks.hw9.dungeon.model;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private int attack;
    private final List<Item> inventory = new ArrayList<>();

    public Player(String name, int hp, int attack) {
        super(name, hp);
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public List<Item> getInventory() {
        return inventory;
    }
}