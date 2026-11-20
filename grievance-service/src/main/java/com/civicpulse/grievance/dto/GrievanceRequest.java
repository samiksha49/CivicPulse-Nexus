package com.civicpulse.grievance.dto;
import com.civicpulse.grievance.entity.*;
import jakarta.validation.constraints.*;
public record GrievanceRequest(@NotNull Long citizenId,@NotNull GrievanceCategory category,@NotNull Severity severity,@NotBlank @Size(max=150) String title,@NotBlank @Size(min=10,max=2000) String description,String location) {}
