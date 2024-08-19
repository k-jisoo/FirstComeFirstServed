package com.sparta.firstcomefirstserved.user.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private String address;
    private String phone;
}
