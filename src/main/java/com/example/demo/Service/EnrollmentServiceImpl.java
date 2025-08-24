package com.example.demo.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.EnrollmentRequestDTO;
import com.example.demo.Entity.Batch;
import com.example.demo.Entity.Enrollment;
import com.example.demo.Entity.EnrollmentInstallment;
import com.example.demo.Entity.InstallmentPlan;
import com.example.demo.Entity.InstallmentPlanItem;
import com.example.demo.Entity.InstallmentTemplate;
import com.example.demo.Entity.User;
import com.example.demo.Repository.BatchRepository;
import com.example.demo.Repository.EnrollmentInstallmentRepository;
import com.example.demo.Repository.EnrollmentRepository;
import com.example.demo.Repository.InstallmentPlanItemRepository;
import com.example.demo.Repository.InstallmentPlanRepository;
import com.example.demo.Repository.InstallmentTemplateRepository;
import com.example.demo.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService  {
	private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentInstallmentRepository installmentRepository;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final InstallmentPlanRepository planRepository;
    private final InstallmentPlanItemRepository planItemRepository;

    /**
     * Enroll user and generate installments based on db plan (planId or batch.assigned plan).
     */
    @Transactional
    public Integer enrollAndGenerateSchedule(EnrollmentRequestDTO dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getUserId()));
        var batch = batchRepository.findById(dto.getBatchId())
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + dto.getBatchId()));

        if (enrollmentRepository.existsByUser_UserIdAndBatch_IdAndActiveTrue(user.getUserId(), batch.getId())) {
            throw new IllegalStateException("User already enrolled in this batch.");
        }

        // decide plan: use dto.planId else batch.installmentPlan else error
        InstallmentPlan plan = null;
        if (dto.getPlanId() != null) {
            plan = planRepository.findById(dto.getPlanId())
                    .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + dto.getPlanId()));
        } else if (batch.getInstallmentPlan() != null) {
            plan = batch.getInstallmentPlan();
        } else {
            throw new IllegalArgumentException("No plan provided and batch has no default plan");
        }

        // load items from DB
        List<InstallmentPlanItem> items = planItemRepository.findByPlan_IdOrderBySeqNoAsc(plan.getId());
        if (items.isEmpty()) throw new IllegalStateException("Plan has no items");

        // enrollment
        var e = new Enrollment();
        e.setUser(user);
        e.setBatch(batch);
        e.setCourse(batch.getCourse());
        e.setStatus("CONFIRMED");
        e.setActive(true);
        e.setEnrolledAt(LocalDateTime.now());
        e = enrollmentRepository.save(e);

        // compute installments amounts
        BigDecimal totalFee = batch.getTotalFee(); // may be null if plan uses FIXED items only
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal[] computed = new BigDecimal[items.size()];

        boolean requiresTotal = items.stream().anyMatch(i -> "PERCENT".equalsIgnoreCase(i.getAmountType()));
        if (requiresTotal && totalFee == null) {
            throw new IllegalArgumentException("Plan requires batch.totalFee (percent-based items) but batch.totalFee is null");
        }

        // compute each amount
        for (int i = 0; i < items.size(); i++) {
            InstallmentPlanItem it = items.get(i);
            if ("FIXED".equalsIgnoreCase(it.getAmountType())) {
                computed[i] = it.getAmountValue().setScale(2, RoundingMode.HALF_UP);
            } else if ("PERCENT".equalsIgnoreCase(it.getAmountType())) {
                // percent of totalFee
                computed[i] = totalFee.multiply(it.getAmountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            } else {
                throw new IllegalArgumentException("Unsupported amountType: " + it.getAmountType());
            }
            sum = sum.add(computed[i]);
        }

        // If plan includes percent items, ensure rounding diff handled so sum==totalFee
        if (requiresTotal) {
            BigDecimal diff = totalFee.subtract(sum).setScale(2, RoundingMode.HALF_UP);
            if (diff.compareTo(BigDecimal.ZERO) != 0) {
                // adjust last installment (add diff)
                int last = computed.length - 1;
                computed[last] = computed[last].add(diff);
            }
        }

        // If plan all FIXED but totalFee exists and differs, we won't auto-adjust (business decision).
        // If you want to force sum==totalFee, you can adjust last element similarly.

        // due date logic: use dto overrides or plan defaults
        int firstShiftDays = plan.getDefaultFirstDueShiftDays();
        int intervalMonths = plan.getDefaultIntervalMonths();

        LocalDate firstDue = e.getEnrolledAt().toLocalDate().plusDays(Math.max(0, firstShiftDays));

        // create EnrollmentInstallment rows
        List<EnrollmentInstallment> schedule = new ArrayList<>();
        for (int i = 0; i < computed.length; i++) {
            EnrollmentInstallment inst = new EnrollmentInstallment();
            inst.setEnrollment(e);
            inst.setSeqNo(items.get(i).getSeqNo());
            inst.setAmount(computed[i]);
            inst.setDueDate(firstDue.plusMonths((long) i * intervalMonths));
            inst.setPayStatus("UNPAID");
            schedule.add(inst);
        }
        installmentRepository.saveAll(schedule);

        return e.getId();
    }
    public boolean isEnrolled(Long userId, Integer batchId) {
        return enrollmentRepository.existsByUser_UserIdAndBatch_IdAndActiveTrue(userId, batchId);
    }

    @Transactional
    public void updateStatus(int enrollmentId, String status) {
        Enrollment e = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found: " + enrollmentId));
        e.setStatus(status);
        if ("CANCELLED".equalsIgnoreCase(status)) {
            e.setActive(false);
            e.setCancelledAt(LocalDateTime.now());
        }
        enrollmentRepository.save(e);
    }

    @Transactional
    public void deleteEnrollment(int enrollmentId, boolean hardDelete) {
        Enrollment e = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found: " + enrollmentId));
        if (hardDelete) {
            enrollmentRepository.delete(e); // consider cascading schedule via FK ON DELETE CASCADE or cleanup manually
        } else {
            e.setActive(false);
            e.setStatus("CANCELLED");
            e.setCancelledAt(LocalDateTime.now());
            enrollmentRepository.save(e);
        }
    }
	
	
	}
