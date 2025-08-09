package ru.rtk.java.homeworks.hw5;

import java.util.Scanner;

public class ArrowText {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите строку из символов '>', '<' и '-': ");
        String input = scanner.nextLine();

        int arrowCount = 0;
        for (int i = 0; i <= input.length() - 5; i++) {
            String sub = input.substring(i, i + 5);

            if (sub.equals(">>-->") || sub.equals("<--<<")) {
                arrowCount++;
                i += 4;
            }
        }

        System.out.println("Количество стрелок: " + arrowCount);
    }
}