package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Dto.CreatePlanRequest;
import com.example.demo.Entity.InstallmentPlan;
import com.example.demo.Entity.InstallmentPlanItem;
import com.example.demo.Repository.BatchRepository;
import com.example.demo.Repository.InstallmentPlanItemRepository;
import com.example.demo.Repository.InstallmentPlanRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstallmentPlanService {
	 private final InstallmentPlanRepository planRepo;
	    private final InstallmentPlanItemRepository itemRepo;
	    private final BatchRepository batchRepo;

	    @Transactional
	    public InstallmentPlan createPlan(CreatePlanRequest req) {
	        if (req.getItems() == null || req.getItems().isEmpty()) {
	            throw new IllegalArgumentException("Plan must have at least one item");
	        }
	        if (planRepo.findByName(req.getName()).isPresent()) {
	            throw new IllegalArgumentException("Plan with same name exists");
	        }

	        InstallmentPlan plan = new InstallmentPlan();
	        plan.setName(req.getName());
	        plan.setDescription(req.getDescription());
	        plan.setDefaultFirstDueShiftDays(req.getDefaultFirstDueShiftDays() == null ? 0 : req.getDefaultFirstDueShiftDays());
	        plan.setDefaultIntervalMonths(req.getDefaultIntervalMonths() == null ? 1 : req.getDefaultIntervalMonths());
	        plan.setActive(true);

	        plan = planRepo.save(plan);

	        List<InstallmentPlanItem> items = new ArrayList<>();
	        for (var it : req.getItems()) {
	            InstallmentPlanItem item = new InstallmentPlanItem();
	            item.setPlan(plan);
	            item.setSeqNo(it.getSeqNo());
	            item.setAmountType(it.getAmountType().toUpperCase());
	            item.setAmountValue(it.getAmountValue());
	            items.add(item);
	        }
	        itemRepo.saveAll(items);
	        plan.setItems(items);

	        return plan;
	    }

	    @Transactional
	    public void assignPlanToBatch(Long planId, Integer batchId) {
	        var plan = planRepo.findById(planId).orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
	        var batch = batchRepo.findById(batchId).orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId));
	        batch.setInstallmentPlan(plan);
	        batchRepo.save(batch);
	    }
}
