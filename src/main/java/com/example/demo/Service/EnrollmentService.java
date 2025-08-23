package com.example.demo.Service;

import com.example.demo.Dto.EnrollmentRequestDTO;

public interface EnrollmentService {
	public Integer enrollAndGenerateSchedule(EnrollmentRequestDTO dto)         ;  // create enrollment
	  boolean isEnrolled(Long userId, Integer batchId);  // check
	  void updateStatus(int enrollmentId, String status);
	  void deleteEnrollment(int enrollmentId, boolean hardDelete);
	  
}
