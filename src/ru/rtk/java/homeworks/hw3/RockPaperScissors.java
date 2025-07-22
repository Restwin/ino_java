package ru.rtk.java.homeworks.hw3;

import java.util.Random;

public class RockPaperScissors {
    public static void main(String[] args) {
        Random rand = new Random();

        int vasyaChoice = rand.nextInt(3);
        int petyaChoice = rand.nextInt(3);

        System.out.println("Вася выбрал: " + choiceToString(vasyaChoice));
        System.out.println("Петя выбрал: " + choiceToString(petyaChoice));

        int result = determineWinner(vasyaChoice, petyaChoice);

        switch (result) {
            case 0:
                System.out.println("Ничья!");
                break;
            case 1:
                System.out.println("Вася выиграл!");
                break;
            case 2:
                System.out.println("Петя выиграл!");
                break;
        }
    }

    /**
     * Метод для преобразования числа в строку
     * @param choice - результат рандома
     * @return Текст вместо цифры
     */
    public static String choiceToString(int choice) {
        return switch (choice) {
            case 0 -> "Камень";
            case 1 -> "Ножницы";
            case 2 -> "Бумага";
            default -> "";
        };
    }

    /**
     * Метод для определения победителя
     * @param vasya - Вася
     * @param petya - Петя
     * @return 0 - ничья, 1 - Вася, 2 - Петя
     */
    public static int determineWinner(int vasya, int petya) {
        if (vasya == petya) {
            return 0; // ничья
        }

        // Логика выигрыша
        if ((vasya == 0 && petya == 1) || // Камень против ножниц
            (vasya == 1 && petya == 2) || // Ножницы против бумаги
            (vasya == 2 && petya == 0))   // Бумага против камня
        {
            return 1; // Вася выигрывает
        } else {
            return 2; // Петя выигрывает
        }
    }
}
