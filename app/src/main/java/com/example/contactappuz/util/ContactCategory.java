package com.example.contactappuz.util;

public enum ContactCategory {
    FAMILY("Family"),
    FRIENDS("Friends"),
    WORK("Work"),
    OTHERS("Others");

    private String name;

    private ContactCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}