package com.csjbot.api.pay.model;

public enum Env {
    PROD,
    DEV,
    TEST
    ;

    public static Env get(String name){
        switch (name){
            case "PROD":
                return PROD;
            case "DEV":
                return DEV;
            case "TEST":
                return TEST;
            default:
                return null;
        }
    }
}
