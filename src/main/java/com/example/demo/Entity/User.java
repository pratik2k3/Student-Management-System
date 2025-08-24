package com.example.demo.Entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;   // Currently verified email
    
    @Column(unique = true)
    private String pendingEmail;  // New email waiting for verification

    private Boolean isEmailVerified = false; // true only when 'email' is verified

    private String mobileNo;
    private String phoneNo;

    private String firstName;
    private String lastName;
    private String address;

    private String role; // admin / student
    private LocalDate createDate;

    private String education;
    private Integer passoutYear;

    private String status; // Active / Inactive

    private String parentContactNo;
    private Integer age;

    private String gender;
    private LocalDate dob;

    private String aadharCard;
    private String uanNo;

    @ManyToMany(mappedBy = "users")
    private List<Batch> batches;

    // Helper method to confirm new email
    public void confirmNewEmail() {
        this.email = this.pendingEmail;
        this.pendingEmail = null;
        this.isEmailVerified = true;
    }
}
