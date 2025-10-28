package com.valencia.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email inválido")
    String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    String password;
}
