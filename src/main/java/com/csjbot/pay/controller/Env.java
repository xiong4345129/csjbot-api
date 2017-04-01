package com.csjbot.pay.controller;

import com.csjbot.controller.FaceController;

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
