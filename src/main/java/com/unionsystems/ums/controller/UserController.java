package com.unionsystems.ums.controller;

import com.unionsystems.ums.interceptor.AuthInterceptor;
import com.unionsystems.ums.model.ChangePasswordRequest;
import com.unionsystems.ums.model.User;
import com.unionsystems.ums.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("create")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PostMapping("update")
    public ResponseEntity<User> update(@RequestBody User user, @RequestHeader(value= AuthInterceptor.CURRENT_USER) User authUser) {
        return ResponseEntity.ok(userService.update(user, authUser));
    }

    @GetMapping("findByEmail/{email}")
    public ResponseEntity<Optional<User>> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

//    @GetMapping("findById/{id}")
//    public ResponseEntity<Optional<User>> findById(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.findById(id));
//    }

    @PostMapping("findAll")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("activateOrDeactivate/{id}")
    public ResponseEntity<?> activateOrDeactivate(@PathVariable Long id) {
        userService.activateOrDeactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, @RequestHeader(value= AuthInterceptor.CURRENT_USER) User authUser) {
        userService.changePassword(request, authUser);
        return ResponseEntity.ok().build();
    }
}
