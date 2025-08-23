package com.example.demo.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Dto.SeedTemplatesDTO;
import com.example.demo.Entity.Batch;
import com.example.demo.Entity.InstallmentTemplate;
import com.example.demo.Repository.BatchRepository;
import com.example.demo.Repository.InstallmentTemplateRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstallmentTemplateService {
	 private final InstallmentTemplateRepository templateRepo;
	    private final BatchRepository batchRepository;

	    // Seeds exactly what you showed:
	    // 1time: 23k
	    // 2time: 13k, 13k
	    // 3time: 5k, 10k, 10k
	    @Transactional
	    public void seedDefaultTemplates(SeedTemplatesDTO dto) {
	        Batch batch = batchRepository.findById(dto.getBatchId())
	                .orElseThrow(() -> new RuntimeException("Batch not found: " + dto.getBatchId()));

	        // clear existing templates for this batch (optional)
	        // templateRepo.deleteAll(templateRepo.findByBatch_Id(...) ) — skip for brevity

	        List<InstallmentTemplate> list = new ArrayList<>();

	        // 1time
	        list.add(build(batch, "1time", 1, bd(23000), dto.getDue1()));

	        // 2time
	        list.add(build(batch, "2time", 1, bd(13000), dto.getDue1()));
	        list.add(build(batch, "2time", 2, bd(13000), dto.getDue2()));

	        // 3time
	        list.add(build(batch, "3time", 1, bd( 6000), dto.getDue1()));
	        list.add(build(batch, "3time", 2, bd(11000), dto.getDue2()));
	        list.add(build(batch, "3time", 3, bd(11000), dto.getDue3()));

	        templateRepo.saveAll(list);
	    }

	    private InstallmentTemplate build(Batch b, String type, int seq, BigDecimal amt, java.time.LocalDate due) {
	        InstallmentTemplate t = new InstallmentTemplate();
	        t.setBatch(b);
	        t.setType(type);
	        t.setSeqNo(seq);
	        t.setAmount(amt);
	        t.setDueDate(due);
	        return t;
	    }

	    private BigDecimal bd(int rupees) {
	        return new BigDecimal(rupees);
	    }
}
