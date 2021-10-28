package com.unionsystems.ums.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Token is required")
    String token;
    @NotBlank(message = "Password is required")
    String password;
}
