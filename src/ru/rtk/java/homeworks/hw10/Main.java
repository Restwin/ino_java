package ru.rtk.java.homeworks.hw10;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Создаем коллекцию (список) автомобилей.
        List<Car> cars = List.of(
                new Car("a123me", "Mercedes", "White", 0, 8300000),
                new Car("b873of", "Volga", "Black", 0, 673000),
                new Car("w487mn", "Lexus", "Grey", 76000, 900000),
                new Car("p987hj", "Volga", "Red", 610, 704340),
                new Car("c987ss", "Toyota", "White", 254000, 761000),
                new Car("o983op", "Toyota", "Black", 698000, 740000),
                new Car("p146op", "BMW", "White", 271000, 850000),
                new Car("u893ii", "Toyota", "Purple", 210900, 440000),
                new Car("l097df", "Toyota", "Black", 108000, 780000),
                new Car("y876wd", "Toyota", "Black", 160000, 1000000)
        );

        // Выводим исходный список для наглядности ---
        System.out.println("Автомобили в базе:");
        System.out.println("Номер   Модель     Цвет    Пробег   Стоимость");
        cars.forEach(System.out::println);
        System.out.println();

        // Используя Java Stream API, выполняем задачи
        // 1) Номера всех автомобилей, имеющих заданный цвет или нулевой пробег.
        String colorToFind = "Black";
        long mileageToFind = 0L;

        String numbersByColorOrMileage = cars.stream()
                .filter(car -> car.getColor().equals(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getNumber)
                .collect(Collectors.joining(" "));

        System.out.println("Номера автомобилей по цвету или пробегу: " + numbersByColorOrMileage);

        // 2) Количество уникальных моделей в ценовом диапазоне.
        long priceFrom = 700_000L;
        long priceTo = 800_000L;

        long uniqueModelsCount = cars.stream()
                .filter(car -> car.getCost() >= priceFrom && car.getCost() <= priceTo)
                .count();

        System.out.println("Уникальные автомобили: " + uniqueModelsCount + " шт.");

        // 3) Цвет автомобиля с минимальной стоимостью.
        String minCostCarColor = cars.stream()
                .min(Comparator.comparingLong(Car::getCost))
                .map(Car::getColor)
                .orElse("Не найдено");

        System.out.println("Цвет автомобиля с минимальной стоимостью: " + minCostCarColor);

        // 4) Средняя стоимость искомой модели.
        String modelToFind = "Toyota";
        OptionalDouble averageCostOptional = cars.stream()
                .filter(car -> car.getModel().equals(modelToFind))
                .mapToLong(Car::getCost)
                .average();

        System.out.printf("Средняя стоимость модели %s: %.2f%n", modelToFind, averageCostOptional.orElse(0.0));

        // Проверка для модели, которой нет в списке
        String modelToFind2 = "Volvo";
        OptionalDouble averageCostOptional2 = cars.stream()
                .filter(car -> car.getModel().equals(modelToFind2))
                .mapToLong(Car::getCost)
                .average();
        System.out.printf("Средняя стоимость модели %s: %.2f%n", modelToFind2, averageCostOptional2.orElse(0.0));
    }
}