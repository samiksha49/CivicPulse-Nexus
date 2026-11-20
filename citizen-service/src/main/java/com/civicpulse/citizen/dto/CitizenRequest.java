package com.civicpulse.citizen.dto;
import com.civicpulse.citizen.entity.Gender;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record CitizenRequest(
 @NotBlank @Size(min=3,max=100) String fullName,
 @NotBlank @Email String email,
 @NotBlank @Pattern(regexp="^[6-9]\\d{9}$",message="Phone must be a valid 10-digit mobile number") String phone,
 @NotBlank @Pattern(regexp="^\\d{12}$",message="Aadhaar must be exactly 12 digits") String aadhaarNumber,
 @NotNull Gender gender,
 @NotNull @Past LocalDate dateOfBirth,
 @NotNull @Min(1) @Max(200) Integer wardNumber,
 @NotBlank @Size(max=255) String address
) {}
