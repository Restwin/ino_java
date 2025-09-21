package ru.rtk.java.homeworks.hw7;

import java.util.Objects;

public class Product {
    private final String name;
    private final double price;

    public Product(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым.");
        }
        if (name.trim().length() < 3) {
            throw new IllegalArgumentException("Название продукта не может быть короче 3 символов.");
        }
        if (name.matches("\\d+")) {
            throw new IllegalArgumentException("Название продукта не должно состоять только из цифр.");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Цена продукта должна быть положительным числом.");
        }

        this.name = name.trim();
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (Цена: %.2f руб.)", name, getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}