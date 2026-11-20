package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.GrievanceStatus;
import jakarta.validation.constraints.Size;
public record StatusUpdateRequest(GrievanceStatus status,@Size(min=10,max=2000) String resolutionNotes,String remarks) {}
