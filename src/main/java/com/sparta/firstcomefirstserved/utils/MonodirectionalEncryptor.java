package com.sparta.firstcomefirstserved.utils;
public interface MonodirectionalEncryptor extends Encryptor{

    @Override
    public String decrypt(String value) throws Exception;

    @Override
    public String encrypt(String value) throws Exception;
}
