package com.example.demo.Service;

import java.util.List;

import com.example.demo.Dto.BatchRequestDTO;
import com.example.demo.Entity.Batch;

public interface BatchService {
		    
	    List<Batch> getAllBatches();

		Object getBatchById(Integer id);
	 Batch createBatch(BatchRequestDTO dto);
		void deleteBatch(Integer id);
}
