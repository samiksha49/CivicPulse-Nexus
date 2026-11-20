
package com.civicpulse.grievance.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.civicpulse.grievance.dto.AssignRequest;
import com.civicpulse.grievance.dto.GrievanceRequest;
import com.civicpulse.grievance.dto.GrievanceResponse;
import com.civicpulse.grievance.dto.StatusUpdateRequest;
import com.civicpulse.grievance.entity.Grievance;
import com.civicpulse.grievance.entity.GrievanceCategory;
import com.civicpulse.grievance.entity.GrievanceHistory;
import com.civicpulse.grievance.entity.GrievanceStatus;
import com.civicpulse.grievance.entity.Severity;
import com.civicpulse.grievance.repository.GrievanceHistoryRepository;
import com.civicpulse.grievance.repository.GrievanceRepository;

@Service
public class GrievanceService {

    private final GrievanceRepository repo;
    private final GrievanceHistoryRepository history;
    private final RestClient citizen;

    private static final List<GrievanceStatus> OPEN = List.of(
        GrievanceStatus.REGISTERED,
        GrievanceStatus.ASSIGNED,
        GrievanceStatus.IN_PROGRESS
    );

    public GrievanceService(
        GrievanceRepository r,
        GrievanceHistoryRepository h,
        org.springframework.core.env.Environment env
    ) {
        repo = r;
        history = h;
        citizen = RestClient.builder()
            .baseUrl(env.getProperty(
                "civic.citizen-service.url",
                "http://localhost:8081"
            ))
            .build();
    }

    @Transactional
    public GrievanceResponse register(GrievanceRequest r) {
        Map c;

        try {
            c = citizen.get()
                .uri("/api/citizens/{id}", r.citizenId())
                .retrieve()
                .body(Map.class);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Citizen not found or Citizen Service unavailable"
            );
        }

        Grievance g = new Grievance();

        g.setCitizenId(r.citizenId());
        g.setCitizenCode((String) c.get("citizenCode"));
        g.setCitizenName((String) c.get("fullName"));
        g.setWardNumber((Integer) c.get("wardNumber"));
        g.setCategory(r.category());
        g.setSeverity(r.severity());
        g.setTitle(r.title());
        g.setDescription(r.description());
        g.setLocation(r.location());
        g.setStatus(GrievanceStatus.REGISTERED);
        g.setDepartmentName(department(r.category()));
        g.setSlaHours(sla(r.category(), r.severity()));
        g.setSlaDueAt(LocalDateTime.now().plusHours(g.getSlaHours()));
        g.setGrievanceNumber("TMP-" + UUID.randomUUID());

        g = repo.save(g);

        g.setGrievanceNumber(String.format(
            "GRV-%d-%06d",
            Year.now().getValue(),
            g.getId()
        ));

        record(
            g, "REGISTERED", null, "REGISTERED",
            "Grievance registered", "Citizen"
        );

        return GrievanceResponse.from(g);
    }

    public List<GrievanceResponse> list(String status) {
        List<Grievance> xs =
            status == null || status.isBlank()
            ? repo.findAllByOrderByCreatedAtDesc()
            : repo.findByStatusOrderByCreatedAtDesc(
                GrievanceStatus.valueOf(status)
            );

        return xs.stream().map(GrievanceResponse::from).toList();
    }

    public GrievanceResponse get(Long id) {
        return GrievanceResponse.from(find(id));
    }

    public List<GrievanceHistory> history(Long id) {
        find(id);
        return history.findByGrievanceIdOrderByAtAsc(id);
    }

    @Transactional
    public GrievanceResponse assign(Long id, AssignRequest r) {
        Grievance g = find(id);

        if (!List.of(
            GrievanceStatus.REGISTERED,
            GrievanceStatus.ASSIGNED
        ).contains(g.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Only REGISTERED or ASSIGNED grievances can be assigned"
            );
        }

        String from = g.getStatus().name();

        g.setAssignedOfficer(r.officer());

        if (r.department() != null && !r.department().isBlank()) {
            g.setDepartmentName(r.department());
        }

        g.setStatus(GrievanceStatus.ASSIGNED);

        record(
            g, "ASSIGNED", from, "ASSIGNED",
            "Assigned to " + r.officer(), "Municipal Admin"
        );

        return GrievanceResponse.from(g);
    }

    @Transactional
    public GrievanceResponse updateStatus(
        Long id, StatusUpdateRequest r
    ) {
        Grievance g = find(id);
        GrievanceStatus next = r.status();
        String from = g.getStatus().name();

        if (next == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Status is required"
            );
        }

        boolean ok =
            (next == GrievanceStatus.IN_PROGRESS &&
                (g.getStatus() == GrievanceStatus.ASSIGNED ||
                 g.getStatus() == GrievanceStatus.REGISTERED))
            ||
            (next == GrievanceStatus.RESOLVED &&
                g.getStatus() == GrievanceStatus.IN_PROGRESS)
            ||
            (next == GrievanceStatus.CLOSED &&
                g.getStatus() == GrievanceStatus.RESOLVED)
            ||
            (next == GrievanceStatus.REJECTED &&
                !List.of(
                    GrievanceStatus.CLOSED,
                    GrievanceStatus.REJECTED
                ).contains(g.getStatus()));

        if (!ok) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Invalid status transition: " + from + " to " + next
            );
        }

        if (next == GrievanceStatus.RESOLVED &&
            (r.resolutionNotes() == null ||
             r.resolutionNotes().trim().length() < 10)) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Resolution notes must contain at least 10 characters"
            );
        }

        g.setStatus(next);

        if (next == GrievanceStatus.RESOLVED) {
            g.setResolutionNotes(r.resolutionNotes());
            g.setResolvedAt(LocalDateTime.now());
        }

        record(
            g, "STATUS_UPDATED", from, next.name(),
            r.remarks() == null ? "Status changed" : r.remarks(),
            "Municipal Officer"
        );

        return GrievanceResponse.from(g);
    }

    @Transactional
    public GrievanceResponse escalate(Long id, String reason) {
        Grievance g = find(id);

        doEscalate(
            g,
            reason == null ? "Manual escalation" : reason
        );

        return GrievanceResponse.from(g);
    }

    @Transactional
    public GrievanceResponse simulateBreach(Long id) {
        Grievance g = find(id);

        if (!OPEN.contains(g.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Only open grievances can be simulated"
            );
        }

        g.setSlaDueAt(LocalDateTime.now().minusMinutes(1));

        return GrievanceResponse.from(g);
    }

    @Transactional
    public int escalateOverdue() {
        int n = 0;

        for (Grievance g :
            repo.findByStatusInAndSlaDueAtBefore(
                OPEN, LocalDateTime.now()
            )) {

            if (g.getEscalationLevel() < 3) {
                doEscalate(g, "Automatic SLA breach");
                n++;
            } else {
                g.setSlaBreached(true);
            }
        }

        return n;
    }

    private void doEscalate(Grievance g, String reason) {
        if (g.getEscalationLevel() >= 3) {
            g.setSlaBreached(true);
            return;
        }

        String from = g.getStatus().name();

        g.setEscalationLevel(g.getEscalationLevel() + 1);
        g.setSlaBreached(true);

        g.setSlaDueAt(
            LocalDateTime.now().plusHours(
                Math.max(1, g.getSlaHours() / 2)
            )
        );

        record(
            g, "ESCALATED", from, from,
            reason + " - level " + g.getEscalationLevel(),
            "SLA Monitor"
        );
    }

    private void record(
        Grievance g,
        String action,
        String from,
        String to,
        String remarks,
        String by
    ) {
        history.save(
            new GrievanceHistory(
                g.getId(), action, from, to, remarks, by
            )
        );
    }

    private Grievance find(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Grievance not found: " + id
            )
        );
    }

    private String department(GrievanceCategory c) {
        return switch (c) {
            case WATER_SUPPLY -> "Water Department";
            case ROADS -> "Public Works";
            case SANITATION -> "Sanitation Department";
            case ELECTRICITY, STREET_LIGHTS -> "Electrical Department";
            case DRAINAGE -> "Drainage Department";
            default -> "General Administration";
        };
    }

    private int sla(GrievanceCategory c, Severity s) {
        int base = switch (s) {
            case CRITICAL -> 12;
            case HIGH -> 48;
            case MEDIUM -> 72;
            case LOW -> 120;
        };

        return c == GrievanceCategory.WATER_SUPPLY
            ? base : base + 24;
    }

    public Map<String, Object> meta() {
        return Map.of(
            "categories",
            Arrays.stream(GrievanceCategory.values())
                .map(Enum::name).toList(),

            "severities",
            Arrays.stream(Severity.values())
                .map(Enum::name).toList(),

            "departments",
            List.of(
                "Water Department",
                "Public Works",
                "Sanitation Department",
                "Electrical Department",
                "Drainage Department",
                "General Administration"
            )
        );
    }

    public Map<String, Object> dashboard() {

        long total = repo.count();

        long open = repo.countByStatusIn(OPEN);

        long resolved =
            repo.countByStatus(GrievanceStatus.RESOLVED) +
            repo.countByStatus(GrievanceStatus.CLOSED);

        long breached =
            repo.countByStatusInAndSlaBreachedTrue(OPEN);

        long breachedAll = repo.countBySlaBreachedTrue();

        long month = repo.countByCreatedAtAfter(
            LocalDate.now().withDayOfMonth(1).atStartOfDay()
        );

        Map<String, Long> by = new LinkedHashMap<>();

        for (GrievanceStatus s : GrievanceStatus.values()) {
            by.put(s.name(), repo.countByStatus(s));
        }

        double rate = total == 0
            ? 0
            : Math.round(resolved * 1000.0 / total) / 10.0;

        // Actual SLA compliance, including closed grievances
        double slaCompliance = total == 0
            ? 100.0
            : Math.round(
                (total - breachedAll) * 1000.0 / total
              ) / 10.0;

        return Map.of(
            "registeredCitizens", citizenCount(),
            "grievancesThisMonth", month,
            "totalGrievances", total,
            "openGrievances", open,
            "resolvedGrievances", resolved,
            "resolutionRate", rate,
            "slaBreachedOpen", breached,
            "slaCompliance", slaCompliance,
            "byStatus", by
        );
    }

    private long citizenCount() {
        try {
            Map m = citizen.get()
                .uri("/api/citizens/count")
                .retrieve()
                .body(Map.class);

            return ((Number) m.get("count")).longValue();

        } catch (Exception e) {
            return -1;
        }
    }
}
