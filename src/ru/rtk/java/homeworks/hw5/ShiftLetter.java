package ru.rtk.java.homeworks.hw5;

import java.util.Scanner;

public class ShiftLetter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите маленькую букву английского алфавита: ");
        String input = scanner.nextLine();

        // Проверка на введенный только один символ
        if (input.length() != 1) {
            System.out.println("Ошибка: Введите ровно один символ.");
            return;
        }

        // Проверка на маленькую букву
        char letter = input.charAt(0);

        if (letter < 'a' || letter > 'z') {
            System.out.println("Ошибка: Введенный символ не является маленькой буквой английского алфавита.");
            return;
        }

        String keyboard = "qwertyuiopasdfghjklzxcvbnm";
        int index = keyboard.indexOf(letter);

        // Проверка на неизвестный символ
        if (index == -1) {
            System.out.println("Ошибка: Внутренняя ошибка. Не удалось найти символ на клавиатуре.");
            return;
        }

        // Вычисление индекса предыдущего символа с учетом замкнутости
        int previousIndex = (index + keyboard.length() - 1) % keyboard.length();

        char previousLetter = keyboard.charAt(previousIndex);
        System.out.println("Буква слева: " + previousLetter);
    }
}