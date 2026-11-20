package com.civicpulse.grievance.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="grievance_history")
public class GrievanceHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long grievanceId; private String action; private String fromStatus; private String toStatus;
 @Column(length=1000) private String remarks; private String performedBy; private LocalDateTime at;
 @PrePersist void stamp(){at=LocalDateTime.now();}
 public GrievanceHistory(){}
 public GrievanceHistory(Long id,String action,String from,String to,String remarks,String by){this.grievanceId=id;this.action=action;this.fromStatus=from;this.toStatus=to;this.remarks=remarks;this.performedBy=by;}
 public Long getId(){return id;} public Long getGrievanceId(){return grievanceId;} public String getAction(){return action;}
 public String getFromStatus(){return fromStatus;} public String getToStatus(){return toStatus;} public String getRemarks(){return remarks;}
 public String getPerformedBy(){return performedBy;} public LocalDateTime getAt(){return at;}
}
