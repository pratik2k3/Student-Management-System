package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Batch;

public interface BatchRepository extends JpaRepository<Batch, Integer> {

}
