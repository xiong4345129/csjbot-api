package com.csjbot.api.pay.util;

import java.util.Random;

public final class RandomGen {
    private static final Random RAND = new Random();
    public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMBER = "1234567890";

    public static String randStr(int length, String charset) {
        final int size = charset.length();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = charset.charAt(RAND.nextInt(size));
        }
        return new String(text);
    }

    public static void main(String[] args) {
        System.out.println(randStr(32, ALPHA+NUMBER+ALPHA.toUpperCase()));
    }
}
