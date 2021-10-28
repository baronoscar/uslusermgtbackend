package com.unionsystems.ums.controller;

import com.unionsystems.ums.model.AuthModel;
import com.unionsystems.ums.model.LoginRequest;
import com.unionsystems.ums.model.ResetPasswordRequest;
import com.unionsystems.ums.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Slf4j
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<AuthModel> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("forgotPassword/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
