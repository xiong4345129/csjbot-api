package com.csjbot.pay.util;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderIdGen {

    private static final AtomicInteger atomicInteger = new AtomicInteger();
    private static final String DEL = "-";
    private static final int LIMIT = 1000000;

    public static String next() {
        return genTime() + DEL + genCnt() + DEL + RandomGen.randStr(3, RandomGen.NUMBER);
    }

    private static String genTime() {
        final String timeStr = String.valueOf(System.currentTimeMillis());
        return timeStr.substring(0,7) + DEL + timeStr.substring(7, 11);
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
            System.out.println(OrderIdGen.next());
        }
    }
}
