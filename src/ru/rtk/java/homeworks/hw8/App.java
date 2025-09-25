package ru.rtk.java.homeworks.hw8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        System.out.println("Задание 1: Уникальные элементы");
        // Список с дубликатами
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(9, 2, 3, 2, 7, 5, 9, 6, 5));
        System.out.println("Исходный список: " + numbers);
        System.out.println("Уникальные элементы: " + CollectionUtils.getUniqueElements(numbers));

        ArrayList<String> words = new ArrayList<>(Arrays.asList("Java", "Python", "Java", "C++", "Python"));
        System.out.println("Исходный список: " + words);
        System.out.println("Уникальные элементы: " + CollectionUtils.getUniqueElements(words));
        System.out.println();

        System.out.println("Задание 2: Проверка на анаграмму");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите первую строку для проверки на анаграмму:");
        String s1 = scanner.nextLine();
        System.out.println("Введите вторую строку:");
        String s2 = scanner.nextLine();
        if (AnagramChecker.areAnagrams(s1, s2)) {
            System.out.printf("Результат: True. Строки \"%s\" и \"%s\" являются анаграммами.\n\n", s1, s2);
        } else {
            System.out.printf("Результат: False. Строки \"%s\" и \"%s\" не являются анаграммами.\n\n", s1, s2);
        }
        // Тестовые примеры из задания
        System.out.println("Проверка встроенных примеров:");
        System.out.println("Бейсбол, бобслей -> " + AnagramChecker.areAnagrams("Бейсбол", "бобслей"));
        System.out.println("Героин, регион -> " + AnagramChecker.areAnagrams("Героин", "регион"));
        System.out.println("Клоака, околка -> " + AnagramChecker.areAnagrams("Клоака", "околка"));
        System.out.println();


        System.out.println("Задание 3: PowerfulSet");
        PowerfulSet powerfulSet = new PowerfulSet();
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(0, 1, 2, 4));
        System.out.println("Set1: " + set1);
        System.out.println("Set2: " + set2);

        // intersection (пересечение)
        Set<Integer> intersectionResult = powerfulSet.intersection(set1, set2);
        System.out.println("Пересечение (intersection): " + intersectionResult); // Ожидаем {1, 2}

        // union (объединение)
        Set<Integer> unionResult = powerfulSet.union(set1, set2);
        System.out.println("Объединение (union): " + unionResult); // Ожидаем {0, 1, 2, 3, 4}

        // relativeComplement (разность)
        Set<Integer> complementResult = powerfulSet.relativeComplement(set1, set2);
        System.out.println("Разность (relativeComplement): " + complementResult); // Ожидаем {3}

        scanner.close();
    }
}