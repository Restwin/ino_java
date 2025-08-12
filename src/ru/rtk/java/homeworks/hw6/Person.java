package ru.rtk.java.homeworks.hw6;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {
    private final String name;
    private double money;
    private final Gender gender;
    private final List<Product> shoppingCart;

    public enum Gender {
        MALE, FEMALE
    }

    public Person(String name, double money, Gender gender) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Имя не может меньше трех символов.");
        }
        if (money < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательными.");
        }
        this.name = name;
        this.money = money;
        this.gender = gender;
        this.shoppingCart = new ArrayList<>();
    }

    public void buyProduct(Product product) {
        if (money >= product.getPrice()) {
            money -= product.getPrice();
            shoppingCart.add(product);
            System.out.println(name + " " + (gender == Gender.MALE ? "купил" : "купила") + " " + product.getName());
        } else {
            System.out.println(name + " не может позволить себе " + product.getName());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " - ");
        if (shoppingCart.isEmpty()) {
            sb.append("Ничего не куплено");
        } else {
            for (int i = 0; i < shoppingCart.size(); i++) {
                sb.append(shoppingCart.get(i).getName());
                if (i < shoppingCart.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Double.compare(person.money, money) == 0 && Objects.equals(name, person.name) && gender == person.gender && Objects.equals(shoppingCart, person.shoppingCart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, money, gender, shoppingCart);
    }
}