package ru.rtk.java.homeworks.hw7;

import ru.rtk.java.homeworks.hw7.Person.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Ввод данных о покупателях
        Map<String, Person> people = new HashMap<>();
        Map<String, Product> products = new HashMap<>();

        System.out.println("Введите покупателей через точку с запятой. Например, Павел Андреевич = 10000 (м); Анна Петровна = 2000 (ж)");
        String line = scanner.nextLine();
        String[] personEntries = line.split(";");
        for (String entry : personEntries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;

            try {
                Person person = getPerson(entry);
                people.put(person.name, person);
            } catch (Exception e) {
                System.out.println("Ошибка при обработке данных покупателя '" + entry + "': " + e.getMessage());
            }
        }

        // 2. Блок ввода продуктов
        System.out.println("Введите продукты через точку с запятой в формате Продукт = цена либо Продукт = цена, скидка, срок действия ГГГ-ММ-ДД. Примеры:");
        System.out.println("для обычного продукта: Хлеб = 40; для скидочного продукта: Торт = 1000, 15, 2025-10-31");
        line = scanner.nextLine();
        String[] productEntries = line.split(";");
        for (String entry : productEntries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;

            try {
                String[] parts = entry.split("=");
                String name = parts[0].trim();
                String details = parts[1].trim();
                String[] detailParts = details.split(",");

                double price = Double.parseDouble(detailParts[0].trim());

                Product product;
                if (detailParts.length == 3) {
                    int discount = Integer.parseInt(detailParts[1].trim());
                    LocalDate expiryDate = LocalDate.parse(detailParts[2].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                    product = new DiscountProduct(name, price, discount, expiryDate);
                } else if (detailParts.length == 1) {
                    product = new Product(name, price);
                } else {
                    throw new IllegalArgumentException("Неверный формат данных продукта.");
                }
                products.put(product.getName(), product);

            } catch (DateTimeParseException e) {
                System.out.println("Ошибка при обработке продукта '" + entry + "': неверный формат даты. Используйте ГГГГ-ММ-ДД.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка при обработке продукта '" + entry + "': неверный формат числа (цена или скидка).");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка при создании продукта '" + entry + "': " + e.getMessage());
            }
        }

        System.out.println("\n--- Доступные товары ---");
        products.values().forEach(System.out::println);
        System.out.println("------------------------\n");

        // 3. Блок обработки покупок
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

        // 4. Вывод результатов
        System.out.println("\n--- Результаты покупок ---");
        for (Person person : people.values()) {
            System.out.println(person);
        }

        scanner.close();
    }

    private static Person getPerson(String entry) {
        String[] parts = entry.split("=");
        String namePart = parts[0].trim();
        String moneyGenderPart = parts[1].trim();

        int genderMarkerStart = moneyGenderPart.lastIndexOf('(');
        String moneyStr = moneyGenderPart.substring(0, genderMarkerStart).trim();
        String genderStr = moneyGenderPart.substring(genderMarkerStart + 1, moneyGenderPart.length() - 1).trim();

        double money = Double.parseDouble(moneyStr);
        Gender gender;
        if ("м".equalsIgnoreCase(genderStr)) {
            gender = Gender.MALE;
        } else if ("ж".equalsIgnoreCase(genderStr)) {
            gender = Gender.FEMALE;
        } else {
            throw new IllegalArgumentException("Неверный формат пола, используйте 'м' или 'ж'.");
        }

        Person person = new Person(namePart, money, gender);
        return person;
    }
}