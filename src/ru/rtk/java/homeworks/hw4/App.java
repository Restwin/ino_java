package ru.rtk.java.homeworks.hw4;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Класс App для демонстрации работы класса Television.
 * Создает несколько экземпляров телевизоров, в том числе
 * с заданными параметрами, параметрами от пользователя и случайными параметрами.
 */
public class App {
    public static void main(String[] args) {
        // Создание статики
        runStaticTV();

        // Создание с клавиатуры и рандомных ТВ
        runAdditionTV();
    }

    /**
     * Метод, демонстрирующий базовую функциональность: создание и управление
     * несколькими предопределенными объектами Television.
     */
    public static void runStaticTV() {
        System.out.println("Демонстрация работы с предопределенными телевизорами");

        // Создаем первый экземпляр класса
        Television livingRoomTV = new Television("Samsung QE65Q90", 65.0);
        System.out.println("\nСоздан новый телевизор для гостиной.");
        livingRoomTV.printStatus();

        // Управляем первым телевизором
        livingRoomTV.turnOn();
        livingRoomTV.setChannel(5);
        livingRoomTV.increaseVolume();
        livingRoomTV.increaseVolume();
        livingRoomTV.printStatus();

        livingRoomTV.turnOff();
        livingRoomTV.printStatus();

        System.out.println("\n===Завершение работы с первым телевизором===\n");

        // Создаем второй экземпляр
        Television kitchenTV = new Television("LG 32LQ63", 32.0);
        System.out.println("Создан новый телевизор для кухни.");
        kitchenTV.printStatus();
        kitchenTV.turnOn();
        kitchenTV.setChannel(999); // Попытка установить неверный канал
        kitchenTV.printStatus();
    }

    /**
     * Метод, демонстрирующий дополнительную функциональность: создание объектов
     * на основе ввода пользователя и случайных данных.
     */
    public static void runAdditionTV() {
        System.out.println("\nДемонстрация создания телевизоров по вводу и со случайными параметрами");

        System.out.println("\nСоздание телевизора на основе данных пользователя");
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Введите модель вашего телевизора: ");
            String userModel = scanner.nextLine();

            System.out.print("Введите диагональ (например, 42,0): ");
            double userDiagonal = scanner.nextDouble();

            Television userTV = new Television(userModel, userDiagonal);
            System.out.println("\nСоздан ваш телевизор!");
            userTV.printStatus();
            userTV.turnOn();
            userTV.setChannel(10);
            userTV.printStatus();

        } catch (InputMismatchException e) {
            System.out.println("Ошибка! Вы ввели диагональ в неверном формате. Пожалуйста, используйте запятую как разделитель (например, 42,5).");
        }
        scanner.close();

        System.out.println("\nСоздание телевизора со случайными параметрами");

        Random random = new Random();

        // Массив с возможными брендами для случайного выбора
        String[] brands = {"Sony", "LG", "Samsung", "Philips", "TCL", "Xiaomi"};

        String randomBrand = brands[random.nextInt(brands.length)];

        String randomModel = randomBrand + " " + (1000 + random.nextInt(9000));

        double randomDiagonal = 24 + (100 - 24) * random.nextDouble();
        randomDiagonal = Math.round(randomDiagonal * 10.0) / 10.0;

        Television randomTV = new Television(randomModel, randomDiagonal);
        System.out.println("Сгенерирован случайный телевизор!");
        randomTV.printStatus();

        randomTV.turnOn();
        randomTV.setChannel(1 + random.nextInt(100));

        int randomVolumeLoops = random.nextInt(20);
        System.out.println("Нажимаем на кнопку увеличения громкости " + randomVolumeLoops + " раз...");
        for (int i = 0; i < randomVolumeLoops; i++) {
            randomTV.increaseVolume();
        }
        randomTV.printStatus();
    }
}