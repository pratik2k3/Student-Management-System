package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
boolean existsByEmail(String email);
	
	
	Optional<Trainer> findByEmail(String email);


	Optional<Trainer> findById(Long trainerId);
	
	
}
