package com.csjbot.api.pay.util;

import java.util.concurrent.atomic.AtomicInteger;

public class RefundNoGen {
    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final String DEL = "-";
    public static final String regex = "[0-9]{7}-[0-9]{4}-[0-9]{4}";  // example: 1491442-4935-0001

    public static boolean check(String val) {
        return val != null && val.matches(regex);
    }

    public static String next() {
        return genTime() + DEL + genCnt()+RandomGen.randStr(1, RandomGen.NUMBER);
    }

    private static String genTime() {
        final String timeStr = String.valueOf(System.currentTimeMillis());
        return timeStr.substring(0, 7) + DEL + timeStr.substring(7, 11);
    }

    private static String genCnt() {
        int cnt = atomicInteger.incrementAndGet() % 1000;
        return String.format("%03d", cnt);
    }

    public static void main(String[] args) {
        for (int i =0; i< 5;i++){
            String no = next();
            System.out.println(no+" "+check(no));
        }
    }
}
