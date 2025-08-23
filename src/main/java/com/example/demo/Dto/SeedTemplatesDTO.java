package com.example.demo.Dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SeedTemplatesDTO {
	  private Integer batchId;
	    private LocalDate due1; // e.g., 2025-01-10
	    private LocalDate due2; // e.g., 2025-02-10
	    private LocalDate due3; // e.g., 2025-03-10
}
