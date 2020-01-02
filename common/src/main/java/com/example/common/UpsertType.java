package com.example.common;

public enum UpsertType {
    DEFAULT(0), DELETE(1), INSERT(2), UPDATE(3);

    private int value;

    UpsertType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}