package ru.rtk.java.homeworks.hw9.dungeon.core;

import ru.rtk.java.homeworks.hw9.dungeon.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// Переработанный класс под доп задание.
// Без помощи AI не обошлось, но интересно было, как он сделает.
// В любом случае, код перепроверен и поправлен.
public class SaveLoad {
    private static final Path SAVE_FILE = Paths.get("dungeon_save.dat");
    private static final Path SCORES_FILE = Paths.get("scores.csv");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String DELIMITER = ";";

    public static void save(GameState state) {
        try (BufferedWriter writer = Files.newBufferedWriter(SAVE_FILE)) {
            // -- Player Section --
            writer.write("[PLAYER]\n");
            Player p = state.getPlayer();
            writer.write(String.join(DELIMITER, p.getName(), String.valueOf(p.getHp()), String.valueOf(p.getAttack()), state.getCurrent().getName(), String.valueOf(state.getScore())) + "\n");
            String inventoryStr = p.getInventory().stream().map(SaveLoad::itemToString).collect(Collectors.joining(","));
            writer.write("inventory=" + inventoryStr + "\n");

            // -- World Section --
            writer.write("\n[WORLD]\n");
            for (Room room : state.getAllRooms().values()) {
                writer.write("room=" + room.getName() + DELIMITER + room.getDescription().replace("\n", "\\n") + "\n");

                if (!room.getItems().isEmpty()) {
                    writer.write("items=" + room.getItems().stream().map(SaveLoad::itemToString).collect(Collectors.joining(",")) + "\n");
                }

                if (room.getMonster() != null) {
                    Monster m = room.getMonster();
                    writer.write("monster=" + String.join(DELIMITER, m.getName(), String.valueOf(m.getLevel()), String.valueOf(m.getHp()), String.valueOf(m.getAttack())) + "\n");
                }

                // Сохранение выходов: Направление:ИмяСоседа:ИмяКлюча
                String exitsStr = room.getNeighbors().entrySet().stream()
                        .map(entry -> {
                            String direction = entry.getKey();
                            String neighborName = entry.getValue().getName();
                            String keyName = room.getLockedExits().get(direction);
                            return direction + ":" + neighborName + (keyName != null ? ":" + keyName : "");
                        }).collect(Collectors.joining(","));

                if (!exitsStr.isEmpty()) {
                    writer.write("exits=" + exitsStr + "\n");
                }
            }
            System.out.println("Игра полностью сохранена в " + SAVE_FILE.toAbsolutePath());
            writeScore(p.getName(), state.getScore());
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить игру", e);
        }
    }

    public static GameState load() {
        if (!Files.exists(SAVE_FILE)) {
            System.out.println("Сохранение не найдено.");
            return null;
        }

        try (BufferedReader reader = Files.newBufferedReader(SAVE_FILE)) {
            GameState state = new GameState();
            Map<String, Room> world = new HashMap<>();

            // Временное хранилище для данных о выходах. Ключ - имя комнаты, значение - строка с выходами.
            Map<String, String> roomExitsData = new HashMap<>();

            String line;
            String currentSection = "";
            Room currentRoomForParsing = null;

            // --- ПЕРВЫЙ ПРОХОД: Создаем объекты, но не связываем их ---
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("[")) {
                    currentSection = line;
                    continue;
                }

                if ("[PLAYER]".equals(currentSection) && !line.contains("=")) {
                    String[] pData = line.split(DELIMITER);
                    Player p = new Player(pData[0], Integer.parseInt(pData[1]), Integer.parseInt(pData[2]));
                    state.setPlayer(p);
                    state.setCurrent(new Room(pData[3], "")); // Заглушка для имени комнаты
                    state.setScore(Integer.parseInt(pData[4]));
                } else {
                    String[] parts = line.split("=", 2);
                    String key = parts[0];
                    String value = parts[1];

                    if ("[PLAYER]".equals(currentSection)) {
                        if ("inventory".equals(key) && !value.isEmpty()) {
                            Arrays.stream(value.split(",")).map(SaveLoad::stringToItem).forEach(state.getPlayer().getInventory()::add);
                        }
                    } else if ("[WORLD]".equals(currentSection)) {
                        switch (key) {
                            case "room":
                                String[] rData = value.split(DELIMITER, 2);
                                currentRoomForParsing = new Room(rData[0], rData[1].replace("\\n", "\n"));
                                world.put(rData[0], currentRoomForParsing);
                                break;
                            case "items":
                                if (currentRoomForParsing != null && !value.isEmpty()) {
                                    Arrays.stream(value.split(",")).map(SaveLoad::stringToItem).forEach(currentRoomForParsing.getItems()::add);
                                }
                                break;
                            case "monster":
                                if (currentRoomForParsing != null) {
                                    String[] mData = value.split(DELIMITER);
                                    currentRoomForParsing.setMonster(new Monster(mData[0], Integer.parseInt(mData[1]), Integer.parseInt(mData[2]), Integer.parseInt(mData[3])));
                                }
                                break;
                            case "exits":
                                if (currentRoomForParsing != null) {
                                    roomExitsData.put(currentRoomForParsing.getName(), value);
                                }
                                break;
                        }
                    }
                }
            }

            // --- ВТОРОЙ ПРОХОД: Связываем комнаты ---
            for (Map.Entry<String, String> entry : roomExitsData.entrySet()) {
                Room room = world.get(entry.getKey());
                String[] exits = entry.getValue().split(",");
                for (String exitData : exits) {
                    String[] exitParts = exitData.split(":");
                    String direction = exitParts[0];
                    String neighborName = exitParts[1];
                    Room neighbor = world.get(neighborName);

                    if (neighbor != null) {
                        if (exitParts.length == 3) { // Запертый выход
                            String keyName = exitParts[2];
                            room.addLockedExit(direction, neighbor, keyName);
                        } else { // Обычный выход
                            room.getNeighbors().put(direction, neighbor);
                        }
                    }
                }
            }

            // --- ФИНАЛИЗАЦИЯ ---
            state.resetWorld(world);
            state.setCurrent(world.get(state.getCurrent().getName())); // Устанавливаем настоящую комнату

            return state;

        } catch (Exception e) {
            System.err.println("Ошибка формата файла сохранения. Не удалось загрузить игру. " + e.getMessage());
            return null;
        }
    }

    private static String itemToString(Item item) {
        String type = item.getClass().getSimpleName();
        return type + ":" + item.getName();
    }

    private static Item stringToItem(String data) {
        String[] parts = data.split(":", 2);
        String type = parts[0];
        String name = parts[1];

        return switch (type) {
            case "Potion" -> new Potion(name, name.contains("Великое") ? 25 : (name.contains("Трофейное")? 10 : 5));
            case "Key" -> new Key(name);
            case "Weapon" -> new Weapon(name, 3);
            default -> throw new IllegalArgumentException("Неизвестный тип предмета: " + type);
        };
    }

    public static void printScores() {
        if (!Files.exists(SCORES_FILE)) {
            System.out.println("Пока нет результатов.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SCORES_FILE)) {
            System.out.println("Таблица лидеров (топ-10):");
            r.lines().skip(1).map(l -> l.split(",")).filter(a -> a.length >= 3)
                    .map(a -> new Score(a[1], Integer.parseInt(a[2])))
                    .sorted(Comparator.comparingInt(Score::score).reversed()).limit(10)
                    .forEach(s -> System.out.println(s.player() + " — " + s.score()));
        } catch (IOException e) {
            System.err.println("Ошибка чтения результатов: " + e.getMessage());
        }
    }

    private static void writeScore(String player, int score) {
        try {
            boolean header = !Files.exists(SCORES_FILE) || Files.size(SCORES_FILE) == 0;
            try (BufferedWriter w = Files.newBufferedWriter(SCORES_FILE, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (header) {
                    w.write("ts,player,score\n");
                }
                w.write(DATE_FORMATTER.format(LocalDateTime.now()) + "," + player + "," + score + "\n");
            }
        } catch (IOException e) {
            System.err.println("Не удалось записать очки: " + e.getMessage());
        }
    }

    private record Score(String player, int score) {}
}