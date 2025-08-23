package com.example.demo.Controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Batch;
import com.example.demo.Entity.Trainer;
import com.example.demo.Service.TrainerService;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
	@Autowired
	private TrainerService trainerService;

	// Create
	@PostMapping
    public ResponseEntity<String> create(@RequestBody Trainer trainer) throws BadRequestException {
        Trainer savedTrainer = trainerService.create(trainer);
        return ResponseEntity.ok("✅ Trainer created successfully with ID: " + savedTrainer.getId());
    }

    // Get all
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Trainer> trainers = trainerService.getAll();
        if (trainers.isEmpty()) {
            return ResponseEntity.ok("⚠️ No trainers found.");
        }
        return ResponseEntity.ok(trainers);
    }

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<String> get(@PathVariable int id) {
        Trainer trainer = trainerService.get(id);
        if (trainer == null) {
            return ResponseEntity.ok("❌ Trainer not found with ID: " + id);
        }
        return ResponseEntity.ok("✅ Trainer found: " + trainer.getFirstName() + " " + trainer.getLastName());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Trainer trainer) throws BadRequestException {
        trainerService.update(id, trainer);
        return ResponseEntity.ok("✅ Trainer updated successfully with ID: " + id);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        trainerService.delete(id);
        return ResponseEntity.ok("🗑️ Trainer deleted successfully with ID: " + id);
    }

    // Assign batch to trainer
    @PutMapping("/{trainerId}/assign/{batchId}")
    public ResponseEntity<String> assignBatch(@PathVariable int trainerId, @PathVariable int batchId) {
        trainerService.assignBatch(trainerId, batchId);
        return ResponseEntity.ok("✅ Batch " + batchId + " assigned to Trainer " + trainerId);
    }

    // Get trainer’s batches
    @GetMapping("/{id}/batches")
    public ResponseEntity<?> getBatches(@PathVariable int id) {
        List<Batch> batches = trainerService.getBatches(id);
        if (batches.isEmpty()) {
            return ResponseEntity.ok("⚠️ No batches assigned to Trainer with ID: " + id);
        }
        return ResponseEntity.ok(batches);
    }
}
