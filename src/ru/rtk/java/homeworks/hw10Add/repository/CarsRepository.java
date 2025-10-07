package ru.rtk.java.homeworks.hw10Add.repository;

import ru.rtk.java.homeworks.hw10Add.model.Car;

import java.util.List;

/**
 * Интерфейс для работы с хранилищем автомобилей (репозиторием).
 */
public interface CarsRepository {

    List<Car> findAll();
}