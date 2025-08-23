package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.BatchRequestDTO;
import com.example.demo.Entity.Batch;
import com.example.demo.Entity.Course;
import com.example.demo.Entity.Trainer;
import com.example.demo.Exception.BatchServiceException;
import com.example.demo.Repository.BatchRepository;
import com.example.demo.Repository.CourseRepository;
import com.example.demo.Repository.TrainerRepository;
@Service
public class BatchServiceImpl implements BatchService {
	@Autowired
	BatchRepository batchRepository;
	
	    @Autowired
	    private TrainerRepository trainerRepository;
	    
	    @Autowired
	    private CourseRepository courseRepository;

	    // Create Batch with Trainer
	    public Batch createBatch(BatchRequestDTO dto) {
	    	 Batch batch = new Batch();
	         batch.setBatchName(dto.getBatchName());
	         batch.setTiming(dto.getTiming());

	         // fetch trainer by ID
	         Trainer trainer = trainerRepository.findById(dto.getTrainerId())
	                 .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + dto.getTrainerId()));
	         batch.setTrainer(trainer);

	         // fetch course by ID
	         Course course = courseRepository.findById(dto.getCourseId())
	                 .orElseThrow(() -> new RuntimeException("Course not found with id: " + dto.getCourseId()));
	         batch.setCourse(course);

	         return batchRepository.save(batch);
	     }
	    

	    public List<Batch> getAllBatches() {
	        return batchRepository.findAll();
	    }

	    public Batch getBatchById(Integer id) {
	        return batchRepository.findById(id)
	                .orElseThrow(() -> new BatchServiceException("Batch not found with id: " + id, HttpStatus.NOT_FOUND));
	    }

	    public void deleteBatch(Integer id) {
	        batchRepository.deleteById(id);
	    }

		

		

		
}
