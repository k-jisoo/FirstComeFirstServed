package com.sparta.firstcomefirstserved.user.dto;

import com.sparta.firstcomefirstserved.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phone;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.phone = user.getPhone();
    }
}
