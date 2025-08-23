package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.InstallmentTemplate;

public interface InstallmentTemplateRepository extends JpaRepository<InstallmentTemplate, Long> {
	  List<InstallmentTemplate> findByBatch_IdAndTypeOrderBySeqNoAsc(Integer batchId, String type);
}
