package com.unionsystems.ums.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String phoneNo;
    @Column(nullable = false)
    private String password;
    @Column()
    private String verifyToken;
    @Column(nullable = false)
    private Boolean emailVerified;
    @Column(nullable = false)
    private Boolean active;

    public void updateUserModel(User user) {
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setPhoneNo(user.getPhoneNo());
    }
}
