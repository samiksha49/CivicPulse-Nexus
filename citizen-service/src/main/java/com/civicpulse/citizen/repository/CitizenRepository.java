package com.civicpulse.citizen.repository;
import com.civicpulse.citizen.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CitizenRepository extends JpaRepository<Citizen,Long> {
 boolean existsByEmail(String v); boolean existsByPhone(String v); boolean existsByAadhaarNumber(String v);
 List<Citizen> findByFullNameContainingIgnoreCase(String name);
 List<Citizen> findByWardNumber(Integer ward);
}
