package ru.rtk.java.homeworks.hw10Add.test;

import ru.rtk.java.homeworks.hw10Add.model.Car;
import ru.rtk.java.homeworks.hw10Add.repository.CarsRepository;
import ru.rtk.java.homeworks.hw10Add.repository.CarsRepositoryImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String dataDir = "src/ru/rtk/java/homeworks/hw10Add/"; // Особенность желания всех домашек в одном месте

        // Создаем объект репозитория, указывая ему, где лежат данные.
        CarsRepository carsRepository = new CarsRepositoryImpl(dataDir + "data/cars.txt");

        // Получаем все автомобили из репозитория.
        List<Car> cars = carsRepository.findAll();

        // Если список пуст (например, файл не найден), выходим.
        if (cars.isEmpty()) {
            System.out.println("База данных автомобилей пуста или не найдена. Программа завершена.");
            return;
        }

        // --- Используем StringBuilder для формирования всего вывода в одну строку ---
        StringBuilder outputBuilder = new StringBuilder();

        outputBuilder.append("Автомобили в базе:\n");
        outputBuilder.append("Number  Model      Color   Mileage  Cost\n");
        cars.forEach(car -> outputBuilder.append(car).append("\n"));
        outputBuilder.append("\n");

        // Далее похоже на то, что было в оригинальном
        // 1) Номера всех автомобилей, имеющих заданный цвет или нулевой пробег.
        String colorToFind = "Black";
        long mileageToFind = 0L;

        String numbersByColorOrMileage = cars.stream()
                .filter(car -> car.getColor().equals(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getNumber)
                .collect(Collectors.joining(" "));

        // Меняем везде способ вывода в отличие от оригинального
        outputBuilder.append("Номера автомобилей по цвету или пробегу: ").append(numbersByColorOrMileage).append("\n");

        // 2) Количество уникальных моделей в ценовом диапазоне.
        long priceFrom = 700_000L;
        long priceTo = 800_000L;

        long uniqueModelsCount = cars.stream()
                .filter(car -> car.getCost() >= priceFrom && car.getCost() <= priceTo)
                .count();

        outputBuilder.append("Уникальные автомобили: ").append(uniqueModelsCount).append(" шт.\n");

        // 3) Цвет автомобиля с минимальной стоимостью.
        String minCostCarColor = cars.stream()
                .min(Comparator.comparingLong(Car::getCost))
                .map(Car::getColor)
                .orElse("Не найдено");

        outputBuilder.append("Цвет автомобиля с минимальной стоимостью: ").append(minCostCarColor).append("\n");

        // 4) Средняя стоимость искомой модели.
        String modelToFind = "Toyota";
        OptionalDouble averageCostOptional = cars.stream()
                .filter(car -> car.getModel().equals(modelToFind))
                .mapToLong(Car::getCost)
                .average();

        outputBuilder.append(String.format("Средняя стоимость модели %s: %.2f\n", modelToFind, averageCostOptional.orElse(0.0)));

        // Проверка для модели, которой нет в списке
        String modelToFind2 = "Volvo";
        OptionalDouble averageCostOptional2 = cars.stream()
                .filter(car -> car.getModel().equals(modelToFind2))
                .mapToLong(Car::getCost)
                .average();
        outputBuilder.append(String.format("Средняя стоимость модели %s: %.2f\n", modelToFind2, averageCostOptional2.orElse(0.0)));

        // Выводим все собранные данные в консоль
        String finalOutput = outputBuilder.toString();
        System.out.println(finalOutput);

        // Записываем результат в файл
        try {

            Path outputPath = Paths.get(dataDir + "data/result.txt");
            Files.writeString(outputPath, finalOutput);
            System.out.println("\nРезультат также сохранен в файл: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Не удалось записать результат в файл: " + e.getMessage());
        }
    }
}