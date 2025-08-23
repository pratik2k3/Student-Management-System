package com.example.demo.Dto;

import lombok.Data;

@Data
public class EnrollmentRequestDTO {
	    private Long userId;
	    private Integer batchId;
	    private Long planId; // optional. if null, service will try batch.getInstallmentPlan()
	    private Integer firstDueShiftDays; // optional override
	    private Integer intervalMonths;
	
}
