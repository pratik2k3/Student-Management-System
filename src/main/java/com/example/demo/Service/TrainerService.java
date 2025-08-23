package com.example.demo.Service;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.example.demo.Entity.Batch;
import com.example.demo.Entity.Trainer;

public interface TrainerService {
	Trainer create(Trainer trainer) throws BadRequestException;
	
	Trainer get(int id);
	
	List<Trainer> getAll();
	
	Trainer update(int id, Trainer trainer) throws BadRequestException;
	
	void delete(int id);
	
	Trainer assignBatch(int trainerId, int batchId);
	
	List<Batch> getBatches(int trainerId);
	
	
	
	
	
}
