package com.example.demo.Dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreatePlanItemDTO {
	 private Integer seqNo;
	    private String amountType; // "FIXED" or "PERCENT"
	    private BigDecimal amountValue; // rupees if FIXED, percent if PERCENT
}
