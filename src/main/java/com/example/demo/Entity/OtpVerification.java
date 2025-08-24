package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.example.demo.utility.OtpPurpose;

@Entity
@Table(name = "otp_verification")
@Data
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String otp;

    private LocalDateTime createdTime;

    private LocalDateTime expiryTime;

    private boolean expired;
    
    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose; // NEW FIELD
    
    
}
