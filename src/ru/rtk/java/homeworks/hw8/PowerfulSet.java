package ru.rtk.java.homeworks.hw8;

import java.util.HashSet;
import java.util.Set;

public class PowerfulSet {

    /**
     * Возвращает пересечение двух наборов (элементы, которые есть в обоих наборах).
     * @return Новое множество, содержащее общие элементы.
     */
    public <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    /**
     * Возвращает объединение двух наборов (все элементы из обоих наборов без дубликатов).
     * @return Новое множество, содержащее все элементы.
     */
    public <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }

    /**
     * Возвращает относительное дополнение (элементы, которые есть в первом наборе, но отсутствуют во втором).
     * @return Новое множество, содержащее разность.
     */
    public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }
}