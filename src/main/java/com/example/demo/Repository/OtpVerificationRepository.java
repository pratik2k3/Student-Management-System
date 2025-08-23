package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.OtpVerification;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    // ✅ Delete expired OTPs
    @Transactional
    void deleteByExpiryTimeBefore(LocalDateTime now);

    // ✅ Find OTP by email (for verifying a specific user)
    Optional<OtpVerification> findByEmail(String email);

    // ✅ Check OTP with email (for validation)
    Optional<OtpVerification> findByEmailAndOtp(String email, String otp);
}
