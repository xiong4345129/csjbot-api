package com.csjbot.api.pay.util;

import java.util.concurrent.atomic.AtomicInteger;

public final class OrderIdGen {

    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final String DEL = "-";
    private static final int LIMIT = 1000000;
    private static final String regex = "[0-9]{7}-[0-9]{4}(-[0-9]{3}){3}";  // example: 1491442-4935-000-001-816

    public static boolean check(String val) {
        return val != null && val.matches(regex);
    }

    public static String next() {
        return genTime() + DEL + genCnt() + DEL + RandomGen.randStr(3, RandomGen.NUMBER);
    }

    private static String genTime() {
        final String timeStr = String.valueOf(System.currentTimeMillis());
        return timeStr.substring(0, 7) + DEL + timeStr.substring(7, 11);
    }

    private static String genCnt() {
        int cnt = atomicInteger.incrementAndGet();
        if (cnt >= LIMIT) cnt = cnt / LIMIT;
        String cntStr = String.format("%06d", cnt);
        return cntStr.substring(0, 3) + DEL + cntStr.substring(3, 6);
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        for (int i = 0; i < 10; i++) {
            String orderId = next();
            System.out.println(orderId);
            System.out.println(check(orderId));
        }
    }
}
