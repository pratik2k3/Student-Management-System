package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.BatchRequestDTO;
import com.example.demo.Entity.Batch;
import com.example.demo.Service.BatchService;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

	 @Autowired
	    private BatchService batchService;

	    // Create Batch under Trainer
	 @PostMapping
	 public ResponseEntity<String> createBatch(@RequestBody BatchRequestDTO batchRequest) {
	     batchService.createBatch(batchRequest);
	     return ResponseEntity
	             .status(HttpStatus.CREATED)
	             .body("Batch created successfully!");
	    }

	    // Get all Batches
	    @GetMapping
	    public ResponseEntity<List<Batch>> getAllBatches() {
	        return ResponseEntity.ok(batchService.getAllBatches());
	    }

	    // Get Batch by ID
	    @GetMapping("/{id}")
	    public ResponseEntity<Object> getBatch(@PathVariable Integer id) {
	        return ResponseEntity.ok(batchService.getBatchById(id));
	    }

	    // Delete Batch
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteBatch(@PathVariable Integer id) {
	        batchService.deleteBatch(id);
	        return ResponseEntity.noContent().build();
	    }
}
