package ru.rtk.java.homeworks.hw9.dungeon.core;

import ru.rtk.java.homeworks.hw9.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        registerCommands();
        bootstrapWorld();
    }

    private void registerCommands() {
        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        commands.put("gc-stats", (ctx, a) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            long mb = 1024 * 1024;
            System.out.println("Память: used=" + (used / mb) + "MB free=" + (free / mb) + "MB total=" + (total / mb) + "MB");
        });
        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));
        commands.put("world-info", (ctx, a) -> WorldInfo.printLog());

        commands.put("move", (ctx, args) -> {
            if (args.isEmpty()) {
                throw new InvalidCommandException("Не указано направление. Пример: move north");
            }
            String direction = args.getFirst().toLowerCase(Locale.ROOT);
            Room currentRoom = ctx.getCurrent();

            String requiredKey = currentRoom.getLockedExits().get(direction);
            if (requiredKey != null) {
                boolean hasKey = ctx.getPlayer().getInventory().stream()
                        .anyMatch(item -> item instanceof Key && item.getName().equals(requiredKey));
                if (!hasKey) {
                    throw new InvalidCommandException("Дверь заперта. Нужен '" + requiredKey + "'.");
                }
                System.out.println("Вы открыли дверь с помощью '" + requiredKey + "'.");
            }

            Room nextRoom = currentRoom.getNeighbors().get(direction);

            if (nextRoom == null) {
                throw new InvalidCommandException("Туда пути нет.");
            }

            ctx.setCurrent(nextRoom);
            System.out.println("Вы перешли в: " + nextRoom.getName());
            System.out.println(nextRoom.describe());
        });

        commands.put("take", (ctx, args) -> {
            if (args.isEmpty()) {
                throw new InvalidCommandException("Не указано название предмета. Пример: take Малое зелье");
            }
            String itemName = String.join(" ", args);
            Room currentRoom = ctx.getCurrent();

            Optional<Item> itemToTake = currentRoom.getItems().stream()
                    .filter(item -> item.getName().equalsIgnoreCase(itemName))
                    .findFirst();

            if (itemToTake.isPresent()) {
                Item item = itemToTake.get();
                currentRoom.getItems().remove(item);
                ctx.getPlayer().getInventory().add(item);
                System.out.println("Взято: " + item.getName());
            } else {
                throw new InvalidCommandException("Такого предмета здесь нет: '" + itemName + "'");
            }
        });

        commands.put("inventory", (ctx, a) -> {
            List<Item> inventory = ctx.getPlayer().getInventory();
            if (inventory.isEmpty()) {
                System.out.println("Инвентарь пуст.");
                return;
            }

            System.out.println("Инвентарь:");
            Map<String, List<Item>> groupedItems = inventory.stream()
                    .collect(Collectors.groupingBy(
                            item -> item.getClass().getSimpleName(),
                            TreeMap::new,
                            Collectors.toList()
                    ));

            groupedItems.forEach((type, items) -> {
                System.out.println("- " + type + " (" + items.size() + "):");
                items.forEach(item -> System.out.println("  * " + item.getName()));
            });
        });

        commands.put("use", (ctx, args) -> {
            if (args.isEmpty()) {
                throw new InvalidCommandException("Не указано название предмета. Пример: use Малое зелье");
            }
            String itemName = String.join(" ", args);
            Optional<Item> itemToUse = ctx.getPlayer().getInventory().stream()
                    .filter(item -> item.getName().equalsIgnoreCase(itemName))
                    .findFirst();

            if (itemToUse.isPresent()) {
                itemToUse.get().apply(ctx);
            } else {
                throw new InvalidCommandException("У вас нет такого предмета: '" + itemName + "'");
            }
        });

        commands.put("fight", (ctx, a) -> {
            Room currentRoom = ctx.getCurrent();
            Monster monster = currentRoom.getMonster();
            Player player = ctx.getPlayer();

            if (monster == null) {
                throw new InvalidCommandException("Здесь не с кем сражаться.");
            }

            // Ход игрока
            int playerDamage = player.getAttack();
            monster.setHp(monster.getHp() - playerDamage);
            System.out.println("Вы бьёте " + monster.getName() + " на " + playerDamage + ". HP монстра: " + Math.max(0, monster.getHp()));
            ctx.addScore(5);

            // Проверка, побежден ли монстр
            if (monster.getHp() <= 0) {
                System.out.println("Вы победили " + monster.getName() + "!");
                currentRoom.setMonster(null);
                currentRoom.getItems().add(new Potion("Трофейное зелье", 10));
                System.out.println("Получите трофейное зелье!");
                ctx.addScore(50);
                return;
            }

            // Ход монстра
            int monsterDamage = monster.getAttack();
            player.setHp(player.getHp() - monsterDamage);
            System.out.println(monster.getName() + " отвечает на " + monsterDamage + ". Ваше HP: " + Math.max(0, player.getHp()));

            // Проверка, побежден ли игрок
            if (player.getHp() <= 0) {
                System.out.println("Вы были повержены... Игра окончена.");
                SaveLoad.save(ctx);
                System.exit(0);
            }
        });

        // Дополнительно: демонстрация проверки GC
        commands.put("alloc", (ctx, a) -> {
            int mb = 42;
            // Выделяем 42 МБ памяти, которые станут мусором сразу после выхода из метода
            byte[] allocation = new byte[mb * 1024 * 1024];
            System.out.println("Выделено " + mb + " МБ памяти для демонстрации GC.");
        });

        commands.put("save", (ctx, a) -> SaveLoad.save(state));
        commands.put("load", (ctx, a) -> {
            GameState loadedState = SaveLoad.load();
            if (loadedState != null) {
                this.state = loadedState;
                System.out.println("Игра и состояние мира успешно загружены.");
                System.out.println(this.state.getCurrent().describe());
            }
        });
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());
        commands.put("exit", (ctx, a) -> {
            System.out.println("Пока!");
            SaveLoad.save(state);
            System.exit(0);
        });
    }

    private void bootstrapWorld() {
        Player hero = new Player("Герой", 20, 5);
        state.setPlayer(hero);

        Room square = new Room("Площадь", "Каменная площадь, в центре красивый сад, а в саду стоит газебо.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет, только тихий вой недалеко.");
        Room cave = new Room("Пещера", "Темно и сыро, но что-то поблескивает.");
        Room treasury = new Room("Сокровищница", "Комната, доверху набитая золотом и драгоценностями. Или нет...");
        square.getNeighbors().put("north", forest);
        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        cave.getNeighbors().put("west", forest);
        square.addLockedExit("west", treasury, "Серебряный ключ");
        treasury.getNeighbors().put("east", square);

        cave.getItems().add(new Key("Серебряный ключ"));
        forest.getItems().add(new Potion("Малое зелье", 5));
        treasury.getItems().add(new Weapon("Огненный топор", 37));

        forest.setMonster(new Monster("Волк", 1, 8, 2));
        square.setMonster(new Monster("Газебо", 2, 42, 100));

        state.getAllRooms().put(square.getName(), square);
        state.getAllRooms().put(forest.getName(), forest);
        state.getAllRooms().put(cave.getName(), cave);
        state.getAllRooms().put(treasury.getName(), treasury);

        state.setCurrent(square);
    }

    public void run() {
        System.out.println("DungeonMini (Restwin Edition). 'help' — команды.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> parts = Arrays.asList(line.split("\s+"));
                String cmd = parts.getFirst().toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                    if (!cmd.equals("save") && !cmd.equals("load") && !cmd.equals("world-info")) {
                        state.addScore(1);
                    }
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}

/*
 Примеры ошибок, которые в коде нельзя оставлять без контроля и обработки

 1. Ошибка компиляции (Compile-time Error)
    Такие ошибки компилятор Java найдет еще до запуска программы.
    Программа с такой ошибкой не скомпилируется в .class файл.
*/
    // Нельзя присвоить число в строку без преобразования.
    // String s = 123;
    // int x = "hello";

/*
 2. Ошибка выполнения (Runtime Error / Exception)
    Эта ошибка возникнет уже во время работы запущенной программы.
    Такие ошибки связаны с логическими проблемами, некорректными данными или внешними условиями.
*/
    // Например, деление на ноль. При выполнении этой строки будет выброшено ArithmeticException.
    // Его лучше предугадать и перехватить в коде
    // int result = 10 / 0;

