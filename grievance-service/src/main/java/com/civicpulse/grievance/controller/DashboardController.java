package com.civicpulse.grievance.controller;
import com.civicpulse.grievance.service.GrievanceService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
 private final GrievanceService service;
 public DashboardController(GrievanceService s){service=s;}
 @GetMapping("/summary") public Map<String,Object> summary(){return service.dashboard();}
}
