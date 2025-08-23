package com.example.demo.Entity;
import jakarta.persistence.Index;
import java.math.BigDecimal;
import java.time.LocalDate;

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
@Data
@Entity
@Table(name = "installment_templates",
       indexes = @Index(name="ux_template", columnList="batch_id,type,seq_no", unique = true))
public class InstallmentTemplate {
	 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // "1time" | "2time" | "3time"
	    @Column(nullable = false, length = 10)
	    private String type;

	    @Column(name="seq_no", nullable = false)
	    private Integer seqNo;

	    @Column(nullable = false, precision = 12, scale = 2)
	    private BigDecimal amount;

	    @Column(nullable = false)
	    private LocalDate dueDate;

	    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	    @JoinColumn(name = "batch_id", nullable = false)
	    private Batch batch;
}
