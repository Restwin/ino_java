package ru.rtk.java.homeworks.hw10Add.repository;

import ru.rtk.java.homeworks.hw10Add.model.Car;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация репозитория. Данные читаются из файла.
 */
public class CarsRepositoryImpl implements CarsRepository {

    private final String filePath;
    private List<Car> carsCache; // Кэш для хранения машин, чтобы не читать файл каждый раз

    /**
     * Конструктор принимает путь к файлу с данными.
     * @param filePath путь к файлу, например "data/cars.txt"
     */
    public CarsRepositoryImpl(String filePath) {
        this.filePath = filePath;
        this.carsCache = null;
    }

    @Override
    public List<Car> findAll() {
        if (carsCache != null) {
            return carsCache;
        }

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.err.println("Файл не найден по пути: " + filePath);
            return Collections.emptyList();
        }

        try {
            List<String> lines = Files.readAllLines(path);

            // Превращаем список строк в список объектов Car
            this.carsCache = lines.stream()
                    .map(this::parseCarFromString)
                    .collect(Collectors.toList());

            return this.carsCache;

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Вспомогательный приватный метод для преобразования одной строки из файла в объект Car.
     * @param line строка вида "a123me|Mercedes|White|0|8300000"
     * @return объект Car
     */
    private Car parseCarFromString(String line) {
        // Разделяем строку по символу "|"
        String[] parts = line.split("\\|");

        String number = parts[0];
        String model = parts[1];
        String color = parts[2];
        long mileage = Long.parseLong(parts[3]);
        long cost = Long.parseLong(parts[4]);

        return new Car(number, model, color, mileage, cost);
    }
}