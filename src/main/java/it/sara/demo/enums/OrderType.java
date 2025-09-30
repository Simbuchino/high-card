package it.sara.demo.enums;

import lombok.Getter;

@Getter
public enum OrderType {
    BY_FIRSTNAME("by firstName"),
    BY_FIRSTNAME_DESC("by firstName desc"),
    BY_LASTNAME("by lastName"),
    BY_LASTNAME_DESC("by lastName");
    private final String displayName;

    OrderType(String displayName) {
        this.displayName = displayName;
    }
}