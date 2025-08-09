package ru.rtk.java.homeworks.hw5;

import java.util.Arrays;
import java.util.Scanner;

public class SortWords {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите два слова в латинской раскладке, разделенных пробелом: ");
        String input = scanner.nextLine();

        // Проверяем на два слова буквами английского алфавита
        boolean valid = input.matches("[a-zA-Z]+ [a-zA-Z]+");

        if (!valid) {
            System.out.println("Ошибка: Введенная последовательность не является двумя словами с использованием английского алфавита.");
            return;
        }

        // Работаем с последовательностью
        String[] words = input.split(" ");

        for (String word : words) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String sortedWord = new String(chars).toLowerCase();

            System.out.print(sortedWord + " ");
        }
        System.out.println();
    }
}