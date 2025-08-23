package com.example.demo.Entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "batches")
@Data
public class Batch {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @Column(nullable = false)
	    private String batchName;

	    @Column
	    private String timing;

	    // Many Batches → One Trainer
	    @ManyToOne
	    @JoinColumn(name = "trainer_id")
	    private Trainer trainer;

	    // Many Batches → One Course
	    @ManyToOne
	    @JoinColumn(name = "course_id")
	    private Course course;

	    // Many Users → Many Batches
	    @ManyToMany
	    @JoinTable(
	        name = "batch_user",
	        joinColumns = @JoinColumn(name = "batch_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id")
	    )
	    private List<User> users;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "installment_plan_id")
	    private InstallmentPlan installmentPlan;
		public BigDecimal getTotalFee() {
			// TODO Auto-generated method stub
			return null;
		}
}
