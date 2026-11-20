package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.*;
import java.time.*;
public record GrievanceResponse(Long id,String grievanceNumber,Long citizenId,String citizenCode,String citizenName,Integer wardNumber,String category,String severity,String title,String description,String location,String departmentName,String assignedOfficer,String status,int escalationLevel,int slaHours,LocalDateTime slaDueAt,boolean slaBreached,String slaStatus,double hoursRemaining,String resolutionNotes,LocalDateTime createdAt) {
 public static GrievanceResponse from(Grievance g){double h=Duration.between(LocalDateTime.now(),g.getSlaDueAt()).toMinutes()/60.0;String s=g.isSlaBreached()||h<0?"BREACHED":h<6?"DUE_SOON":"WITHIN_SLA";return new GrievanceResponse(g.getId(),g.getGrievanceNumber(),g.getCitizenId(),g.getCitizenCode(),g.getCitizenName(),g.getWardNumber(),g.getCategory().name(),g.getSeverity().name(),g.getTitle(),g.getDescription(),g.getLocation(),g.getDepartmentName(),g.getAssignedOfficer(),g.getStatus().name(),g.getEscalationLevel(),g.getSlaHours(),g.getSlaDueAt(),g.isSlaBreached(),s,Math.round(h*10.0)/10.0,g.getResolutionNotes(),g.getCreatedAt());}
}
