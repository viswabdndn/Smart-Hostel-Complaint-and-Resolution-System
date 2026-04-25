package com.smarthostel.controller;

import com.smarthostel.model.Complaint;
import com.smarthostel.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/*
 * REST endpoints that expose ComplaintService. Mirrors what the old
 * Main.java called after a successful student login, and the
 * "View All Complaints" admin option:
 *
 * POST /api/complaints            -> addComplaint(studentId, type, description)
 * GET  /api/complaints            -> getAllComplaints()
 * GET  /api/complaints/{id}       -> getComplaintById(id)
 * GET  /api/complaints/sorted     -> sortByStudentId()
 */
@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<Complaint> addComplaint(@RequestBody Map<String, Object> body) {

        int studentId      = Integer.parseInt(body.get("studentId").toString());
        String type        = body.get("type").toString();
        String description = body.get("description").toString();

        Complaint saved = complaintService.addComplaint(studentId, type, description);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<Collection<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable int id) {
        Complaint c = complaintService.getComplaintById(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Complaint>> sortByStudentId() {
        return ResponseEntity.ok(complaintService.sortByStudentId());
    }
}
