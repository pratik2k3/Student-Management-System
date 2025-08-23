package com.example.demo.Entity;
import jakarta.persistence.Index;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import lombok.Data;
@Data
@Entity
@Table(name = "enrollment_installments",
       indexes = @Index(name="ux_enroll_seq", columnList="enrollment_id,seq_no", unique = true))
public class EnrollmentInstallment {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name="seq_no", nullable = false)
    private Integer seqNo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false, length = 10)
    private String payStatus = "UNPAID"; // UNPAID/PAID

    private LocalDateTime paidAt;
	
	
	
}
