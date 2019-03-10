package com.beehive.riki.common;

public enum Priority {
    LOW(1),
    MID(2),
    HIGH(3);

    private final int value;

    Priority(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
