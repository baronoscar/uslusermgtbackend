package com.unionsystems.ums.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Email is required")
    String current;
    @NotBlank(message = "Password is required")
    String password;
}
