package ru.rtk.java.homeworks.hw7;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DiscountProduct extends Product {
    private final int discountPercentage;
    private final LocalDate discountExpiryDate;

    public DiscountProduct(String name, double price, int discountPercentage, LocalDate discountExpiryDate) {
        super(name, price);

        if (discountPercentage <= 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Скидка должна быть в диапазоне от 1 до 100 процентов.");
        }
        if (discountExpiryDate == null) {
            throw new IllegalArgumentException("Срок действия скидки должен быть указан.");
        }

        this.discountPercentage = discountPercentage;
        this.discountExpiryDate = discountExpiryDate;
    }

    @Override
    public double getPrice() {
        if (isDiscountActive()) {
            double basePrice = super.getPrice();
            return basePrice - (basePrice * discountPercentage / 100.0);
        } else {
            return super.getPrice();
        }
    }

    private boolean isDiscountActive() {
        return !LocalDate.now().isAfter(discountExpiryDate);
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getDiscountExpiryDate() {
        return discountExpiryDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = discountExpiryDate.format(formatter);
        if (isDiscountActive()) {
            return String.format("%s (Цена со скидкой %d%%: %.2f руб., действует до %s)",
                    getName(), discountPercentage, getPrice(), formattedDate);
        } else {
            return String.format("%s (Цена: %.2f руб., скидка %d%% истекла %s)",
                    getName(), getPrice(), discountPercentage, formattedDate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DiscountProduct that = (DiscountProduct) o;
        return discountPercentage == that.discountPercentage && Objects.equals(discountExpiryDate, that.discountExpiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discountPercentage, discountExpiryDate);
    }
}