package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.InstallmentPlanItem;

public interface InstallmentPlanItemRepository extends JpaRepository<InstallmentPlanItem, Long> {
	 List<InstallmentPlanItem> findByPlan_IdOrderBySeqNoAsc(Long planId);
	}

