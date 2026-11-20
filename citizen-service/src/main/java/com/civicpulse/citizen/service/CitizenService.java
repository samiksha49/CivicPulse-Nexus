package com.civicpulse.citizen.service;
import com.civicpulse.citizen.dto.*;
import com.civicpulse.citizen.entity.*;
import com.civicpulse.citizen.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.Year;
import java.util.*;
@Service
public class CitizenService {
 private final CitizenRepository repo;
 public CitizenService(CitizenRepository repo){this.repo=repo;}
 @Transactional public CitizenResponse register(CitizenRequest r){
  if(repo.existsByEmail(r.email().toLowerCase())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
  if(repo.existsByPhone(r.phone())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Phone already registered");
  if(repo.existsByAadhaarNumber(r.aadhaarNumber())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Aadhaar already registered");
  Citizen c=new Citizen(); c.setFullName(r.fullName().trim()); c.setEmail(r.email().toLowerCase().trim()); c.setPhone(r.phone()); c.setAadhaarNumber(r.aadhaarNumber()); c.setGender(r.gender()); c.setDateOfBirth(r.dateOfBirth()); c.setWardNumber(r.wardNumber()); c.setAddress(r.address().trim()); c.setCitizenCode("TMP-"+UUID.randomUUID());
  c=repo.save(c); c.setCitizenCode(String.format("CTZ-%d-%06d",Year.now().getValue(),c.getId())); return CitizenResponse.from(c);
 }
 public List<CitizenResponse> list(String name,Integer ward){List<Citizen> xs=ward!=null?repo.findByWardNumber(ward):name!=null&&!name.isBlank()?repo.findByFullNameContainingIgnoreCase(name):repo.findAll(); return xs.stream().map(CitizenResponse::from).toList();}
 public CitizenResponse get(Long id){return repo.findById(id).map(CitizenResponse::from).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Citizen not found: "+id));}
 public long count(){return repo.count();}
}
