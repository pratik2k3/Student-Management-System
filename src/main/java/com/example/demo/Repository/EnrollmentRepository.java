package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
	 boolean existsByUser_UserIdAndBatch_IdAndActiveTrue(Long userId, Integer batchId);

	  Optional<Enrollment> findByUser_UserIdAndBatch_IdAndActiveTrue(Long userId, Integer batchId);

	  List<Enrollment> findByUser_UserIdAndActiveTrue(Long userId);
	
	
	
}
