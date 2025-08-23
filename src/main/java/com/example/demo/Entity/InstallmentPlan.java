package com.example.demo.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "installment_plans")
public class InstallmentPlan {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    private String description;

    // defaults used if caller doesn't provide firstDueShiftDays / intervalMonths at enrollment time
    @Column(nullable = false)
    private Integer defaultFirstDueShiftDays = 0;

    @Column(nullable = false)
    private Integer defaultIntervalMonths = 1;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("seqNo ASC")
    private List<InstallmentPlanItem> items;
}
