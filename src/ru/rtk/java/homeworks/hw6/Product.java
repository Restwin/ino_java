package ru.rtk.java.homeworks.hw6;

import java.util.Objects;

public record Product(String name, double price) {
    public Product {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Цена продукта не может быть отрицательной.");
        }
    }

    @Override
    public String toString() {
        return name + " (" + price + ")";
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

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}