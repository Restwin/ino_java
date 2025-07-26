package ru.rtk.java.homeworks.hw4;

/**
 * Класс Television описывает модель телевизора с базовыми функциями.
 * Он содержит состояние (включен/выключен, канал, громкость)
 * и предоставляет методы для управления этим состоянием.
 */
public class Television {

    // Характеристики телевизора
    // Постоянные
    private final String model;      // Модель телевизора
    private final double diagonal;   // Диагональ в дюймах

    // Переменные
    private boolean isOn;            // Состояние: true - включен, false - выключен
    private int currentChannel;      // Текущий номер канала
    private int currentVolume;       // Текущий уровень громкости

    // Константы для ограничений
    private static final int MAX_CHANNEL = 100;
    private static final int MIN_CHANNEL = 1;
    private static final int MAX_VOLUME = 50;
    private static final int MIN_VOLUME = 0;


    // Конструктор
    public Television(String model, double diagonal) {
        this.model = model;
        this.diagonal = diagonal;

        this.isOn = false;
        this.currentChannel = MIN_CHANNEL;
        this.currentVolume = 10;
    }

    // Методы
    public void turnOn() {
        if (!isOn) {
            isOn = true;
            System.out.println("Телевизор " + model + " включен.");
        } else {
            System.out.println("Телевизор " + model + " уже включен.");
        }
    }

    public void turnOff() {
        if (isOn) {
            isOn = false;
            System.out.println("Телевизор " + model + " выключен.");
        } else {
            System.out.println("Телевизор " + model + " уже выключен.");
        }
    }

    public void setChannel(int newChannel) {
        if (!isOn) {
            System.out.println("Невозможно переключить канал, телевизор " + model + " выключен.");
            return;
        }
        if (newChannel >= MIN_CHANNEL && newChannel <= MAX_CHANNEL) {
            this.currentChannel = newChannel;
            System.out.println("Телевизор " + model + ": переключен на канал " + currentChannel);
        } else {
            System.out.println("Телевизор " + model + ": неверный номер канала. Доступны каналы с " + MIN_CHANNEL + " по " + MAX_CHANNEL + ".");
        }
    }

    public void increaseVolume() {
        if (!isOn) {
            System.out.println("Невозможно изменить громкость, телевизор " + model + " выключен.");
            return;
        }
        if (currentVolume < MAX_VOLUME) {
            currentVolume++;
            System.out.println("Телевизор " + model + ": громкость " + currentVolume);
        } else {
            System.out.println("Телевизор " + model + ": максимальная громкость!");
        }
    }

    public void decreaseVolume() {
        if (!isOn) {
            System.out.println("Невозможно изменить громкость, телевизор " + model + " выключен.");
            return;
        }
        if (currentVolume > MIN_VOLUME) {
            currentVolume--;
            System.out.println("Телевизор " + model + ": громкость " + currentVolume);
        } else {
            System.out.println("Телевизор " + model + ": звук выключен.");
        }
    }

    public void printStatus() {
        System.out.println("\nСтатус телевизора: " + this.model);
        System.out.println("Модель: " + this.model);
        System.out.println("Диагональ: " + this.diagonal + "\"");
        System.out.println("Состояние: " + (this.isOn ? "Включен" : "Выключен"));
        if (this.isOn) {
            System.out.println("Текущий канал: " + this.currentChannel);
            System.out.println("Текущая громкость: " + this.currentVolume);
        }
    }

    // Свойства

    public String getModel() {
        return model;
    }

    public double getDiagonal() {
        return diagonal;
    }

    public boolean isOn() {
        return isOn;
    }

    public int getCurrentChannel() {
        return currentChannel;
    }

    public int getCurrentVolume() {
        return currentVolume;
    }
}