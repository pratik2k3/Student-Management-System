package com.example.demo.Service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Batch;
import com.example.demo.Entity.Trainer;
import com.example.demo.Exception.UserServiceException;
import com.example.demo.Repository.BatchRepository;
import com.example.demo.Repository.TrainerRepository;
@Service
public class TrainerServiceImpl implements TrainerService {
	@Autowired
	private TrainerRepository trainerRepository;

	@Autowired
	private BatchRepository batchRepository;
	@Override
	public Trainer create(Trainer trainer) throws BadRequestException {
		  if (trainer.getEmail() == null || trainer.getEmail().isBlank()) {
		        throw new BadRequestException("Email is required");
		    }
		    if (trainerRepository.existsByEmail(trainer.getEmail())) {
		        throw new BadRequestException("Trainer email already exists");
		    }
		    return trainerRepository.save(trainer);
	}

	@Override
	public Trainer get(int id) {
		// TODO Auto-generated method stub
		return trainerRepository.findById(id)
	            .orElseThrow(() -> new UserServiceException("Trainer not found with id " + id,HttpStatus.NOT_FOUND));
	}

	@Override
	public List<Trainer> getAll() {
		// TODO Auto-generated method stub
		return trainerRepository.findAll();
	}

	@Override
	public Trainer update(int id, Trainer trainer) throws BadRequestException {
		// TODO Auto-generated method stub
		Trainer existing = get(id);
	    if (trainer.getFirstName() != null) existing.setFirstName(trainer.getFirstName());
	    if (trainer.getLastName() != null) existing.setLastName(trainer.getLastName());
	    if (trainer.getEmail() != null && !trainer.getEmail().equals(existing.getEmail())) {
	        if (trainerRepository.existsByEmail(trainer.getEmail())) {
	            throw new BadRequestException("Trainer email already exists");
	        }
	        existing.setEmail(trainer.getEmail());
	    }
	    if (trainer.getPhone() != null) existing.setPhone(trainer.getPhone());
	    if (trainer.getSpecialization() != null) existing.setSpecialization(trainer.getSpecialization());
	    return trainerRepository.save(existing);
	}

	@Override
	public void delete(int id) {
		 Trainer existing = get(id);
		    // Optional: detach all batches or block delete if batches assigned
		    trainerRepository.delete(existing);
		
	}

	@Override
	public Trainer assignBatch(int trainerId, int batchId) {
		// TODO Auto-generated method stub
		Trainer trainer = get(trainerId);
	    Batch batch = batchRepository.findById(batchId)
	            .orElseThrow(() -> new UserServiceException("Batch not found with id " + batchId, HttpStatus.NOT_FOUND));
	    // Assign
	    batch.setTrainer(trainer);
	    batchRepository.save(batch);
	    return trainer; // return trainer (or batch) as per your preference
	}

	@Override
	public List<Batch> getBatches(int trainerId) {
		 Trainer trainer = get(trainerId);
		    // If trainer.getBatches() is LAZY, this will load when accessed within transaction; here it’s fine
		    return trainer.getBatches();
		}
	}


