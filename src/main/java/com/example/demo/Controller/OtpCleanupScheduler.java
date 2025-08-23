package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.Service.OtpService;

@Component
public class OtpCleanupScheduler {

    @Autowired
    private OtpService otpService;

    // Run every 30 minutes (1800000 ms)
    // Adjust as per your need
    @Scheduled(fixedRate = 1800000) 
    public void cleanOtpRecords() {
        otpService.cleanExpiredOtps();
    }
}
