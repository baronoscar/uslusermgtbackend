package com.unionsystems.ums.service;

import com.unionsystems.ums.model.*;
import com.unionsystems.ums.repository.UserRepository;
import com.unionsystems.ums.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final MailService mailService;

    public UserService(UserRepository userRepository, JwtUtils jwtUtils, MailService mailService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.mailService = mailService;
    }

    public User create(User user) {
        user.setEmailVerified(Boolean.TRUE);
        user.setActive(Boolean.TRUE);
        validateRequest(user); // validate if user email already exists in the system
        String plainPass = user.getPassword();
        String password = UserService.hashPassword(user.getPassword()); // hash user password
        user.setPassword(password);
        String token  = generateToken(); // generate random 6 digit token
        user.setVerifyToken(token);
        User saved = userRepository.save(user);
        log.info("User created successfully..");
        mailService.sendPassword(plainPass, user.getEmail(), user.getFirstName()); // send token for email verification
        return saved;
    }

    public User update(User user, User authUser) {
        User foundUser = userRepository.findById(authUser.getId()).orElseThrow(() -> new EntityNotFoundException("User with id " + authUser.getId() + " not found"));
        foundUser.updateUserModel(user);
        validateRequest(userRepository.save(user)); // validate if user email already exists in the system
        return userRepository.save(foundUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private void validateRequest(User user) {
        log.info("Id {}", user.getId());
        log.info("email {}", user.getEmail());
        if (user.getId() == null) {
            boolean exists = this.findByEmail(user.getEmail()).isPresent();
            if (exists) {
                throw new EntityExistsException("User with this " + user.getEmail() + " email address already exists");
            }
            return;
        }
        boolean exists = userRepository.findByEmailAndIdNot(user.getEmail(), user.getId()).isPresent();
        if (exists) {
            throw new EntityExistsException("User with this " + user.getEmail() + " email address already exists");
        }
    }

    public static String hashPassword(String password) {
        return ENCODER.encode(password);
    }

    public AuthModel login(LoginRequest request) {
        log.info("Logging in a user");
        User user = this.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Authentication Failed! Email or Password is not valid"));
        if (!ENCODER.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Email or Password not valid");
        }
        userRepository.findByEmailAndEmailVerifiedIsTrue(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Please verify your account before you proceed"));
        userRepository.findByEmailAndActiveIsTrue(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Your Account is Inactive. Please Contact the System Administrator"));
        return generateAuthModel(user);
    }

    private AuthModel generateAuthModel(User user) {
        return AuthModel.builder().token(jwtUtils.generateJwtToken(user)).user(user).build();
    }

    public void changePassword(ChangePasswordRequest request, User currentUser) {
        String email = currentUser.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        boolean matches = ENCODER.matches(request.getCurrent(), user.getPassword());
        if (!matches) {
            throw new BadCredentialsException("Your current password is incorrect");
        }
        String password = UserService.hashPassword(request.getPassword());
        user.setPassword(password);
        userRepository.save(user); // update user password and set the token to null
        log.info("Password change successful!!");
    }

    public void activateOrDeactivateUser(Long id) {
        User found = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        found.setActive(Boolean.FALSE.equals(found.getActive()) ? Boolean.TRUE : Boolean.FALSE);
        userRepository.save(found);
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("User with email " + request.getEmail() + " not found"));
        if (!user.getVerifyToken().equals(request.getToken())) {
            throw new BadCredentialsException("The verification token is invalid");
        }
        String password = UserService.hashPassword(request.getPassword());
        user.setPassword(password);
        user.setVerifyToken(null);
        userRepository.save(user); // update user password and set the token to null
        log.info("Password reset successful!!");
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        String token  = generateToken(); // generate random 6 digit token
        user.setVerifyToken(token);
        userRepository.save(user); // update user and set the token
        this.sendToken(user, token);
        log.info("Forgot password initiated..");
    }

    private void sendToken(User user, String token) {
//        mailService.sendOTP(token, user.getEmail(), user.getFirstName()); // send email
    }

    private String generateToken() {
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d", number);
    }

//    public Object findById(Long id) {
//    }
}
