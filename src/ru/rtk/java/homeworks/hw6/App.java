package ru.rtk.java.homeworks.hw6;

import ru.rtk.java.homeworks.hw6.Person.Gender;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Ввод данных о покупателях и продуктах
        Map<String, Person> people = new HashMap<>();
        Map<String, Product> products = new HashMap<>();

        System.out.println("Введите покупателей через точку с запятой. Например, Павел Андреевич = 10000 (м); Анна Петровна = 2000 (ж) ");
        String line;
        line = scanner.nextLine();
        String[] personEntries = line.split(";");
        for (String entry : personEntries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;

            String[] parts = entry.split("=");
            if (parts.length != 2) {
                System.out.println("Неверный формат ввода покупателя: " + entry);
                System.exit(1);
            }

            String nameAndMoney = parts[0].trim();
            String moneyAndGender = parts[1].trim();

            String name = nameAndMoney.trim();
            double money;
            Gender gender;

            int genderStartIndex = moneyAndGender.indexOf('(');
            if (genderStartIndex == -1) {
                System.out.println("Не указан пол для покупателя " + name);
                System.exit(1);
            }
            String moneyString = moneyAndGender.substring(0, genderStartIndex).trim();
            String genderString = moneyAndGender.substring(genderStartIndex + 1, moneyAndGender.length() - 1).trim();

            try {
                money = Double.parseDouble(moneyString);
                gender = genderString.equalsIgnoreCase("м") ? Gender.MALE : (genderString.equalsIgnoreCase("ж") ? Gender.FEMALE : null);
                if (gender == null) {
                    System.out.println("Неверно указан пол для " + name + ". Используйте 'м' или 'ж'.");
                    System.exit(1);
                }
                Person person = new Person(name, money, gender);
                people.put(name, person);

            } catch (NumberFormatException e) {
                System.out.println("Неверный формат денег для " + name);
                System.exit(1);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при создании покупателя " + name + ": " + e.getMessage());
                System.exit(1);
            }
        }


        System.out.println("Введите продукты через точку с запятой. Например, Хлеб = 40; Молоко = 60 ");
        line = scanner.nextLine();
        String[] productEntries = line.split(";");
        for (String entry : productEntries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;
            String[] parts = entry.split("=");

            if (parts.length != 2) {
                System.out.println("Неверный формат ввода продукта: " + entry);
                System.exit(1);
            }

            String name = parts[0].trim();
            double price;

            try {
                price = Double.parseDouble(parts[1].trim());
                Product product = new Product(name, price);
                products.put(name, product);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат цены для " + name);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при создании продукта " + name + ": " + e.getMessage());
            }
        }


        // 2. Обработка покупок
        System.out.println("Введите покупки в формате: Имя покупателя Название продукта (например, Павел Андреевич - Хлеб). Введите END для завершения.");
        while (!(line = scanner.nextLine()).equalsIgnoreCase("END")) {
            String[] parts = line.split("-");
            if (parts.length != 2) {
                System.out.println("Неверный формат ввода покупки. Попробуйте еще раз.");
                continue;
            }
            String personName = parts[0].trim();
            String productName = parts[1].trim();

            Person person = people.get(personName);
            Product product = products.get(productName);

            if (person == null) {
                System.out.println("Покупатель с именем '" + personName + "' не найден.");
            } else if (product == null) {
                System.out.println("Продукт с названием '" + productName + "' не найден.");
            } else {
                person.buyProduct(product);
            }
        }

        // 3. Вывод результатов
        System.out.println("\n--- Результаты ---");
        for (Person person : people.values()) {
            System.out.println(person);
        }

        scanner.close();
    }
}