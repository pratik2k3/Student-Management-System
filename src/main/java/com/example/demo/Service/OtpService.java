package com.example.demo.Service;

import com.example.demo.Entity.OtpVerification;
import com.example.demo.Entity.User;
import com.example.demo.Repository.OtpVerificationRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.utility.OtpPurpose;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpVerificationRepository otpRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;

    // Generate random 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Send OTP
    public void sendOtp(String email, OtpPurpose purpose) throws MessagingException {
        String otp = generateOtp();

        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setPurpose(purpose);
        otpEntity.setCreatedTime(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(10)); // 10 min validity
        otpEntity.setExpired(false);

        otpRepo.save(otpEntity);

        // Send email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Your OTP Code");
        helper.setText("Your OTP is: " + otp + "\nIt is valid for 10 minutes.");

        mailSender.send(message);
    }

    // Verify OTP
    public String verifyOtp(String email, String otp, OtpPurpose purpose) {
        Optional<OtpVerification> otpData = otpRepo.findByEmailAndOtpAndPurpose(email, otp, purpose);

        if (otpData.isEmpty()) {
            return "Invalid OTP!";
        }

        OtpVerification otpEntity = otpData.get();

        if (otpEntity.isExpired()) {
            return "OTP is already used or expired!";
        }

        if (LocalDateTime.now().isAfter(otpEntity.getExpiryTime())) {
            otpEntity.setExpired(true);
            otpRepo.save(otpEntity);
            return "OTP expired!";
        }

        // Mark OTP as used
        otpEntity.setExpired(true);
        otpRepo.save(otpEntity);

        if (purpose == OtpPurpose.SIGNUP_VERIFY) {
            // ✅ Mark user as verified after signup
            userRepo.findByEmail(email).ifPresent(user -> {
                user.setIsEmailVerified(true);
                userRepo.save(user);
            });
            return "Signup email verified successfully!";
        }

        if (purpose == OtpPurpose.EMAIL_CHANGE) {
            // ✅ Apply the new email only after OTP is verified
            userRepo.findByPendingEmail(email).ifPresent(user -> {
                user.setEmail(user.getPendingEmail());
                user.setPendingEmail(null);
                user.setIsEmailVerified(true);
                userRepo.save(user);
            });
            return "Email updated and verified successfully!";
        }

        return "OTP verified successfully!";
    }

    // Remove expired OTPs
    public void cleanExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        otpRepo.deleteByExpiryTimeBefore(now);
        System.out.println("Expired OTPs cleaned at: " + now);
    }
}
