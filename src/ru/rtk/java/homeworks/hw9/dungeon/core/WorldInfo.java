package ru.rtk.java.homeworks.hw9.dungeon.core;

public final class WorldInfo {
    private static final StringBuilder log = new StringBuilder();

    static {
        log.append("[static init WorldInfo]\n");
        ClassLoader cl = WorldInfo.class.getClassLoader();
        log.append("ClassLoader: ").append(cl).append("\n");
        if (cl != null) log.append("Parent: ").append(cl.getParent()).append("\n");
    }

    public static void touch(String who) {
        log.append("touched by ").append(who).append("\n");
    }

    public static void printLog() {
        System.out.println("--- WorldInfo Log ---");
        System.out.println(log.toString().trim());
        System.out.println("---------------------");
    }

    private WorldInfo() {
    }
}