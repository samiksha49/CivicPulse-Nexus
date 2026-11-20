package com.civicpulse.grievance.entity;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="grievances")
public class Grievance {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String grievanceNumber;
 @Column(nullable=false) private Long citizenId;
 private String citizenCode; private String citizenName; private Integer wardNumber;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private GrievanceCategory category;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Severity severity;
 @Column(nullable=false) private String title;
 @Column(nullable=false,length=2000) private String description;
 private String location; private String departmentName; private String assignedOfficer;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private GrievanceStatus status;
 private int escalationLevel; private int slaHours;
 private LocalDateTime slaDueAt; private boolean slaBreached;
 @Column(length=2000) private String resolutionNotes;
 private LocalDateTime resolvedAt;
 private LocalDateTime createdAt;
 @PrePersist void onCreate(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getGrievanceNumber(){return grievanceNumber;} public void setGrievanceNumber(String v){grievanceNumber=v;}
 public Long getCitizenId(){return citizenId;} public void setCitizenId(Long v){citizenId=v;}
 public String getCitizenCode(){return citizenCode;} public void setCitizenCode(String v){citizenCode=v;}
 public String getCitizenName(){return citizenName;} public void setCitizenName(String v){citizenName=v;}
 public Integer getWardNumber(){return wardNumber;} public void setWardNumber(Integer v){wardNumber=v;}
 public GrievanceCategory getCategory(){return category;} public void setCategory(GrievanceCategory v){category=v;}
 public Severity getSeverity(){return severity;} public void setSeverity(Severity v){severity=v;}
 public String getTitle(){return title;} public void setTitle(String v){title=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public String getLocation(){return location;} public void setLocation(String v){location=v;}
 public String getDepartmentName(){return departmentName;} public void setDepartmentName(String v){departmentName=v;}
 public String getAssignedOfficer(){return assignedOfficer;} public void setAssignedOfficer(String v){assignedOfficer=v;}
 public GrievanceStatus getStatus(){return status;} public void setStatus(GrievanceStatus v){status=v;}
 public int getEscalationLevel(){return escalationLevel;} public void setEscalationLevel(int v){escalationLevel=v;}
 public int getSlaHours(){return slaHours;} public void setSlaHours(int v){slaHours=v;}
 public LocalDateTime getSlaDueAt(){return slaDueAt;} public void setSlaDueAt(LocalDateTime v){slaDueAt=v;}
 public boolean isSlaBreached(){return slaBreached;} public void setSlaBreached(boolean v){slaBreached=v;}
 public String getResolutionNotes(){return resolutionNotes;} public void setResolutionNotes(String v){resolutionNotes=v;}
 public LocalDateTime getResolvedAt(){return resolvedAt;} public void setResolvedAt(LocalDateTime v){resolvedAt=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
