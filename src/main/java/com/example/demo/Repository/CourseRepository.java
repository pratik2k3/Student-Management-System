package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Course;
import com.example.demo.Entity.Trainer;

public interface CourseRepository extends JpaRepository<Course, Integer> {

	Optional<Course> findById(Long courseId);

}
