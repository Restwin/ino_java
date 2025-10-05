package ru.rtk.java.homeworks.hw9.dungeon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Room {
    private final String name;
    private final String description;
    private final Map<String, Room> neighbors = new HashMap<>();
    private final Map<String, String> lockedExits = new HashMap<>();
    private final List<Item> items = new ArrayList<>();
    private Monster monster;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Room> getNeighbors() {
        return neighbors;
    }

    public List<Item> getItems() {
        return items;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster m) {
        this.monster = m;
    }

    public Map<String, String> getLockedExits() {
        return lockedExits;
    }

    public void addLockedExit(String direction, Room room, String keyName) {
        this.neighbors.put(direction, room);
        this.lockedExits.put(direction, keyName);
    }

    public String describe() {
        StringBuilder sb = new StringBuilder(name + ": " + description);
        if (!items.isEmpty()) {
            sb.append("\nПредметы: ").append(items.stream().map(Item::getName).collect(Collectors.joining(", ")));
        }
        if (monster != null) {
            sb.append("\nВ комнате монстр: ").append(monster.getName()).append(" (ур. ").append(monster.getLevel()).append(")");
        }
        if (!neighbors.isEmpty()) {
            List<String> exitsDescription = new ArrayList<>();
            for (Map.Entry<String, Room> entry : neighbors.entrySet()) {
                String direction = entry.getKey();
                if (lockedExits.containsKey(direction)) {
                    exitsDescription.add(direction + " (заперто)");
                } else {
                    exitsDescription.add(direction);
                }
            }
            sb.append("\nВыходы: ").append(String.join(", ", exitsDescription));
        }
        return sb.toString();
    }
}