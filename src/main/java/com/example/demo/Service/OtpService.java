package com.example.demo.Service;

import com.example.demo.Entity.OtpVerification;
import com.example.demo.Repository.OtpVerificationRepository;
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
	    private OtpVerificationRepository otpVerificationRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpVerificationRepository otpRepo;

    // Generate random 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Send OTP
    public void sendOtp(String email) throws MessagingException {
        String otp = generateOtp();

        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedTime(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(10)); // 10 min validity
        otpEntity.setExpired(false);

        otpRepo.save(otpEntity);

        // send email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Your OTP Code");
        helper.setText("Your OTP is: " + otp + "\nIt is valid for 10 minutes.");

        mailSender.send(message);
    }

    // Verify OTP
    public String verifyOtp(String email, String otp) {
        Optional<OtpVerification> otpData = otpRepo.findByEmailAndOtp(email, otp);

        if (otpData.isPresent()) {
            OtpVerification otpEntity = otpData.get();
            if (otpEntity.isExpired()) {
                return "OTP is already used or expired!";
            }
            if (LocalDateTime.now().isAfter(otpEntity.getExpiryTime())) {
                otpEntity.setExpired(true);
                otpRepo.save(otpEntity);
                return "OTP expired!";
            }

            otpEntity.setExpired(true); // mark as used
            otpRepo.save(otpEntity);
            return "OTP verified successfully!";
        } else {
            return "Invalid OTP!";
        }
        
         
    }
    public void cleanExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        otpVerificationRepository.deleteByExpiryTimeBefore(now);
        System.out.println("Expired OTPs cleaned at: " + now);
    }
    
    
}
