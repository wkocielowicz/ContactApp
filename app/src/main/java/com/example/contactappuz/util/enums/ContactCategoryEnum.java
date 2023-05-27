package com.example.contactappuz.util.enums;

/**
 * Enum representing contact categories.
 */
public enum ContactCategoryEnum {
    FAMILY("Family"),
    FRIENDS("Friends"),
    WORK("Work"),
    OTHERS("Others");

    private String name;

    /**
     * Constructs a ContactCategoryEnum with the specified name.
     *
     * @param name The name of the contact category.
     */
    private ContactCategoryEnum(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the contact category.
     *
     * @return The name of the contact category.
     */
    public String getName() {
        return name;
    }
}
