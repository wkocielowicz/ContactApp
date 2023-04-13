package com.example.contactappuz.util.enums;

public enum ContactCategoryEnum {
    FAMILY("Family"),
    FRIENDS("Friends"),
    WORK("Work"),
    OTHERS("Others");

    private String name;

    private ContactCategoryEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}