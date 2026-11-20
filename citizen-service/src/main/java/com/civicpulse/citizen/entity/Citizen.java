package com.civicpulse.citizen.entity;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="citizens")
public class Citizen {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String citizenCode;
 @Column(nullable=false) private String fullName;
 @Column(nullable=false,unique=true) private String email;
 @Column(nullable=false,unique=true,length=10) private String phone;
 @Column(nullable=false,unique=true,length=12) private String aadhaarNumber;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Gender gender;
 @Column(nullable=false) private LocalDate dateOfBirth;
 @Column(nullable=false) private Integer wardNumber;
 @Column(nullable=false) private String address;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 @PrePersist void created(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getCitizenCode(){return citizenCode;} public void setCitizenCode(String v){citizenCode=v;}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
 public String getAadhaarNumber(){return aadhaarNumber;} public void setAadhaarNumber(String v){aadhaarNumber=v;}
 public Gender getGender(){return gender;} public void setGender(Gender v){gender=v;}
 public LocalDate getDateOfBirth(){return dateOfBirth;} public void setDateOfBirth(LocalDate v){dateOfBirth=v;}
 public Integer getWardNumber(){return wardNumber;} public void setWardNumber(Integer v){wardNumber=v;}
 public String getAddress(){return address;} public void setAddress(String v){address=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
