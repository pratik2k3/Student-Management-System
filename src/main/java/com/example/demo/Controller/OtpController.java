package com.example.demo.Controller;

import com.example.demo.Service.OtpService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    // Send OTP
    @PostMapping("/send")
    public String sendOtp(@RequestParam String email) throws MessagingException {
        otpService.sendOtp(email);
        return "OTP sent successfully to " + email;
    }

    // Verify OTP
    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return otpService.verifyOtp(email, otp);
    }
}
