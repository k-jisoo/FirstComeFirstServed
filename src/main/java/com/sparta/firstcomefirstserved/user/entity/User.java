package com.sparta.firstcomefirstserved.user.entity;

import com.sparta.firstcomefirstserved.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Base64;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name="verificationToken")
    private String verificationToken;

    @Column(name="verified", nullable = false)
    private boolean verified = false;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public User(SignupRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.address = requestDto.getAddress();
        this.phone = requestDto.getPhone();
        this.verificationToken = generateRandomString(20);
    }

    public User(String username, String password, String email, String address, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.verificationToken = generateRandomString(20);
    }

    public String generateRandomString(int length) {
        // SecureRandom 사용하여 예측 불가능한 랜덤 값 생성
        SecureRandom random = new SecureRandom();

        // 15 바이트의 랜덤 바이트 배열 생성 (Base64로 인코딩 시 약 20자리 문자열 생성됨)
        byte[] randomBytes = new byte[15];
        random.nextBytes(randomBytes);

        // URL-safe Base64 인코딩
        String randomString = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // 필요한 길이만큼 잘라서 반환
        return randomString.substring(0, Math.min(randomString.length(), length));
    }
}
