package com.sparta.firstcomefirstserved.utils;

public interface BidirectionalEncryptor extends Encryptor {

    @Override
    public String encrypt(String value) throws Exception;

    @Override
    public String decrypt(String value) throws Exception;
}
