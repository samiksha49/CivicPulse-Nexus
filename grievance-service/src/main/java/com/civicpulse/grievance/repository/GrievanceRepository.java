
package com.civicpulse.grievance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.civicpulse.grievance.entity.Grievance;
import com.civicpulse.grievance.entity.GrievanceStatus;

public interface GrievanceRepository extends JpaRepository<Grievance, Long> {

    List<Grievance> findAllByOrderByCreatedAtDesc();

    List<Grievance> findByStatusOrderByCreatedAtDesc(GrievanceStatus status);

    List<Grievance> findByStatusInAndSlaDueAtBefore(
        List<GrievanceStatus> statuses, LocalDateTime now
    );

    long countByStatus(GrievanceStatus status);

    long countByCreatedAtAfter(LocalDateTime time);

    long countByStatusIn(List<GrievanceStatus> statuses);

    long countByStatusInAndSlaBreachedTrue(List<GrievanceStatus> statuses);

    // Count all SLA-breached grievances, including closed grievances
    long countBySlaBreachedTrue();
}
