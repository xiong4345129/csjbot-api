package com.csjbot.api.pay.util;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumGen {

    public static final String MD5="MD5";
    public static final String SHA1="SHA1";

    public static String compute(String checksumAlg, String src, Charset charSet)
        throws NoSuchAlgorithmException {
        final byte[] hashBytes = MessageDigest.getInstance(checksumAlg)
            .digest(src.getBytes(charSet));
        return DatatypeConverter.printHexBinary(hashBytes);
    }

    public static void dumpString(String str){
        System.out.println("CharSet Default "+ Charset.defaultCharset());
        dumpBytes(str.getBytes());
        System.out.println("CharSet UTF8");
        dumpBytes(str.getBytes(StandardCharsets.UTF_8));
    }

    public static void dumpBytes(byte[] bytes){
        for (byte b:bytes){ System.out.print(b+" "); }
    }
}
