package com.example.demo.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.OtpVerification;
import com.example.demo.utility.OtpPurpose;

import jakarta.transaction.Transactional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    @Transactional
    void deleteByExpiryTimeBefore(LocalDateTime now);

    Optional<OtpVerification> findByEmail(String email);

    Optional<OtpVerification> findByEmailAndOtp(String email, String otp);

    Optional<OtpVerification> findByEmailAndOtpAndPurpose(String email, String otp, OtpPurpose purpose);
}
