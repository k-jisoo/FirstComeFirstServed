package com.sparta.firstcomefirstserved.utils;

public interface Encryptor {
    public String encrypt(String value) throws Exception;

    public String decrypt(String value) throws Exception;
}
