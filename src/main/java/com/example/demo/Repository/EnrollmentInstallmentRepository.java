package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.EnrollmentInstallment;

public interface EnrollmentInstallmentRepository extends JpaRepository<EnrollmentInstallment, Long> {

}
