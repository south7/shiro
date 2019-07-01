package com.example.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class MD5Test {
    @Test
    public void testMD5(){
        String hashName="md5";
        String password="123456";
        SimpleHash simpleHash = new SimpleHash(hashName, password, null, 2);
        System.out.println(simpleHash);
    }
}
