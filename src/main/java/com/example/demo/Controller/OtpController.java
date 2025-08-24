package com.example.demo.Controller;
import com.example.demo.Dto.SendOtpRequestDTO;
import com.example.demo.Dto.VerifyOtpRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Service.OtpService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    // ✅ Send OTP API
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody SendOtpRequestDTO request) throws MessagingException {
        otpService.sendOtp(request.getEmail(), request.getPurpose());
        return ResponseEntity.ok("✅ OTP sent successfully to " + request.getEmail() 
                                 + " for purpose: " + request.getPurpose());
    }

    // ✅ Verify OTP API
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequestDTO request) {
        String result = otpService.verifyOtp(request.getEmail(), request.getOtp(), request.getPurpose());

        if ("OTP verified successfully".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
