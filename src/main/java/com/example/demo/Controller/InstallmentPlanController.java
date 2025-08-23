package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.CreatePlanRequest;
import com.example.demo.Entity.InstallmentPlan;
import com.example.demo.Service.InstallmentPlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class InstallmentPlanController {
	 private final InstallmentPlanService planService;

	    @PostMapping
	    public ResponseEntity<Map<String, Object>> createPlan(@RequestBody CreatePlanRequest req) {
	        InstallmentPlan plan = planService.createPlan(req);
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(Map.of("message", "Plan created", "planId", plan.getId()));
	    }

	    @PostMapping("/{planId}/assign-to-batch/{batchId}")
	    public ResponseEntity<Map<String, Object>> assignToBatch(@PathVariable Long planId, @PathVariable Integer batchId) {
	        planService.assignPlanToBatch(planId, batchId);
	        return ResponseEntity.ok(Map.of("message", "Plan assigned to batch"));
	    }
}
