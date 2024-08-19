package com.sparta.firstcomefirstserved.config;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordEncryption {

    // 비밀번호 해싱
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    // 비밀번호 검증
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
