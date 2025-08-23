package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.EnrollmentRequestDTO;
import com.example.demo.Dto.UpdateEnrollmentStatusDTO;
import com.example.demo.Service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
	 private final EnrollmentService enrollmentService;

	  // Enroll user into a batch
	  @PostMapping({"", "/enroll"})
	    public ResponseEntity<Map<String, Object>> enroll(@RequestBody EnrollmentRequestDTO dto) {
	        Integer enrollmentId = enrollmentService.enrollAndGenerateSchedule(dto);
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(Map.of("message", "Enrollment successful", "enrollmentId", enrollmentId));
	    }
	  // Check if enrolled
	  @GetMapping("/check")
	  public ResponseEntity<Map<String, Object>> check(
	      @RequestParam Long userId,
	      @RequestParam Integer batchId) {
	    boolean enrolled = enrollmentService.isEnrolled(userId, batchId);
	    return ResponseEntity.ok(Map.of("enrolled", enrolled));
	  }

	  // Update status (REQUESTED/CONFIRMED/CANCELLED/COMPLETED)
	  @PutMapping("/{id}/status")
	  public ResponseEntity<Map<String, Object>> updateStatus(
	      @PathVariable int id,
	      @RequestBody UpdateEnrollmentStatusDTO body) {
	    enrollmentService.updateStatus(id, body.getStatus());
	    return ResponseEntity.ok(Map.of("message", "Status updated to " + body.getStatus()));
	  }

	  // Delete (soft by default; hardDelete=true for hard delete)
	  @DeleteMapping("/{id}")
	  public ResponseEntity<Map<String, Object>> delete(
	      @PathVariable int id,
	      @RequestParam(defaultValue = "false") boolean hardDelete) {
	    enrollmentService.deleteEnrollment(id, hardDelete);
	    return ResponseEntity.ok(Map.of("message", hardDelete ? "Enrollment hard-deleted" : "Enrollment cancelled (soft delete)"));
	  }
	}
