package com.civicpulse.grievance.scheduler;
import com.civicpulse.grievance.service.GrievanceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class EscalationScheduler {
 private final GrievanceService service;
 public EscalationScheduler(GrievanceService s){service=s;}
 @Scheduled(fixedDelayString="${civic.escalation.check-interval-ms:60000}",initialDelay=10000)
 public void checkSla(){service.escalateOverdue();}
}
