package com.sensor.common;

public enum AccessLevel {

    NORMAL(0, "normal user"),
    MANAGER(1, "manager"),
    ADMIN(2, "admin");

    private final int code;
    private final String msg;

    AccessLevel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

