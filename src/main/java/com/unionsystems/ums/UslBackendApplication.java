package com.unionsystems.ums;

import com.unionsystems.ums.model.User;
import com.unionsystems.ums.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UslBackendApplication implements ApplicationRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(UslBackendApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = new User();
        user.setEmail("usl@aol.com");
        user.setFirstName("union");
        user.setLastName("system");
        user.setPhoneNo("08036663408");
        user.setPassword("union123");
        userService.create(user);
        log.info("Default User saved");
    }
}


