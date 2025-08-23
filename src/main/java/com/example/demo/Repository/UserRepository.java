package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	 boolean existsByEmail(String email);
	 Optional<User> findByUsername(String username);
	 boolean existsByUsername(String username);
	
}
