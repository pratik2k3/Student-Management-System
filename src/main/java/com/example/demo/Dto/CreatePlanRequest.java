package com.example.demo.Dto;

import java.util.List;

import lombok.Data;

@Data
public class CreatePlanRequest {
	private String name;
    private String description;
    private Integer defaultFirstDueShiftDays;
    private Integer defaultIntervalMonths;
   private List<CreatePlanItemDTO> items;
}
