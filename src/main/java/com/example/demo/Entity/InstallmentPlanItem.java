package com.example.demo.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Index;
@Data
@Entity
@Table(name = "installment_plan_items",
       indexes = @Index(name = "ix_plan_seq", columnList = "plan_id, seq_no"))
public class InstallmentPlanItem {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // which plan
	    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	    @JoinColumn(name = "plan_id", nullable = false)
	    private InstallmentPlan plan;

	    @Column(name = "seq_no", nullable = false)
	    private Integer seqNo;

	    @Column(nullable = false, length = 10)
	    private String amountType; // "FIXED" or "PERCENT"

	    // For FIXED -> value in rupees (e.g., 6000). For PERCENT -> percent value (e.g., 50 -> 50%).
	    @Column(nullable = false, precision = 12, scale = 2)
	    private BigDecimal amountValue;
}
