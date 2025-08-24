package com.example.demo.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EnrollmentRequestDTO {
	    private Long userId;
	    private Integer batchId;
	    private Long planId; // optional. if null, service will try batch.getInstallmentPlan()
	   @JsonIgnore
	    private Integer firstDueShiftDays; // optional override
	   @JsonIgnore
	   private Integer intervalMonths;
	
}
