package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
@Entity
@Table(
  name = "enrollments",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "batch_id", "active"})
  }
)

@Data
public class Enrollment {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private int id;

	  @ManyToOne(optional = false, fetch = FetchType.LAZY)
	  @JoinColumn(name = "user_id", nullable = false)
	  private User user;

	  @ManyToOne(optional = false, fetch = FetchType.LAZY)
	  @JoinColumn(name = "batch_id", nullable = false)
	  private Batch batch;

	  @ManyToOne(optional = false, fetch = FetchType.LAZY)
	  @JoinColumn(name = "course_id", nullable = false)
	  private Course course;

	  @Column(nullable = false, updatable = false)
	  private LocalDateTime enrolledAt = LocalDateTime.now();

	  @Column(nullable = false, length = 20)
	  private String status = "CONFIRMED"; // REQUESTED / CONFIRMED / CANCELLED / COMPLETED

	  @Column(nullable = false)
	  private boolean active = true;

	  private LocalDateTime cancelledAt;
}
