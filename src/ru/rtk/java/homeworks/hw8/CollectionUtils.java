package ru.rtk.java.homeworks.hw8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class CollectionUtils {

    /**
     * Возвращает коллекцию уникальных элементов из исходного списка.
     *
     * @param list Исходный список с элементами типа T. Может содержать дубликаты.
     * @param <T>  Открытый тип элементов в списке.
     * @return Новая коллекция типа Set, содержащая только уникальные элементы.
     */
    public static <T> Collection<T> getUniqueElements(ArrayList<T> list) {
        return new HashSet<>(list);
    }
}