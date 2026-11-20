package com.civicpulse.citizen.dto;
import com.civicpulse.citizen.entity.Citizen;
import java.time.*;
public record CitizenResponse(Long id,String citizenCode,String fullName,String email,String phone,String maskedAadhaar,String gender,LocalDate dateOfBirth,Integer wardNumber,String address,LocalDateTime createdAt) {
 public static CitizenResponse from(Citizen c){String a=c.getAadhaarNumber(); return new CitizenResponse(c.getId(),c.getCitizenCode(),c.getFullName(),c.getEmail(),c.getPhone(),"XXXX-XXXX-"+a.substring(a.length()-4),c.getGender().name(),c.getDateOfBirth(),c.getWardNumber(),c.getAddress(),c.getCreatedAt());}
}
