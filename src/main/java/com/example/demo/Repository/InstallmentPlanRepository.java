package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.InstallmentPlan;

public interface InstallmentPlanRepository extends JpaRepository<InstallmentPlan, Long> {
	Optional<InstallmentPlan> findByName(String name);
}
