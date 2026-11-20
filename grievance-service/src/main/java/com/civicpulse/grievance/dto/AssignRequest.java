package com.civicpulse.grievance.dto;
import jakarta.validation.constraints.NotBlank;
public record AssignRequest(@NotBlank String officer,String department) {}
