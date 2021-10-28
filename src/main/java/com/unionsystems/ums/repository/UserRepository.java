package com.unionsystems.ums.repository;

import com.unionsystems.ums.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIdNot(String email, Long id);
    Optional<User> findByEmailAndEmailVerifiedIsTrue(String email);
    Optional<User> findByEmailAndActiveIsTrue(String email);
}
