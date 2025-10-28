package com.valencia.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private Role role;

}
