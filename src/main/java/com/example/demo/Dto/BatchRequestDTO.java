package com.example.demo.Dto;

import lombok.Data;

@Data
public class BatchRequestDTO {
	 private String batchName;
	    private String timing;
	    private Long trainerId;  // pass trainerId in request body
	    private Long courseId;
}
