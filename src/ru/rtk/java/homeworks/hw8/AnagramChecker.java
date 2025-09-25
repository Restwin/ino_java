package ru.rtk.java.homeworks.hw8;

import java.util.Arrays;

public class AnagramChecker {

    /**
     * Проверяет, являются ли две строки анаграммами друг друга.
     * Дополнительно, проверка нечувствительна к регистру и игнорирует все символы, кроме букв.
     *
     * @param s1 Первая строка.
     * @param s2 Вторая строка.
     * @return true, если строки являются анаграммами, иначе false.
     */
    public static boolean areAnagrams(String s1, String s2) {
        // 1. Базовая проверка на null. Если хотя бы одна строка null, они не могут быть анаграммами.
        if (s1 == null || s2 == null) {
            return false;
        }

        // 2. Подготовка строк:
        String cleanedS1 = s1.replaceAll("[^\\p{L}]", "").toLowerCase();
        String cleanedS2 = s2.replaceAll("[^\\p{L}]", "").toLowerCase();

        // 3. Если после очистки строки имеют разную длину, они точно не анаграммы.
        if (cleanedS1.length() != cleanedS2.length()) {
            return false;
        }

        // 4. Преобразуем строки в массивы символов.
        char[] charArray1 = cleanedS1.toCharArray();
        char[] charArray2 = cleanedS2.toCharArray();

        // 5. Сортируем оба массива.
        Arrays.sort(charArray1);
        Arrays.sort(charArray2);

        // 6. Сравниваем отсортированные массивы. Если они идентичны, строки являются анаграммами.
        return Arrays.equals(charArray1, charArray2);
    }
}