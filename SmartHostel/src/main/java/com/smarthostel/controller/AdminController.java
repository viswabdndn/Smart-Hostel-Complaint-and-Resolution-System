package com.smarthostel.controller;

import com.smarthostel.model.Complaint;
import com.smarthostel.service.AdminService;
import com.smarthostel.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * REST endpoints that cover every option of the old Main.java ADMIN MENU:
 *
 *   1. View All Complaints   -> GET  /api/complaints          (ComplaintController)
 *   2. Assign Complaint      -> POST /api/admin/assign
 *   3. Resolve Complaint     -> POST /api/admin/resolve
 *   4. Generate Report       -> GET  /api/admin/report
 *   5. Logout                -> handled client-side (stateless)
 *
 * Plus admin login:
 *   POST /api/admin/login
 *
 * AdminService's logic is unchanged; these endpoints only call its
 * methods. After assign/resolve the controller calls complaintService.save(c)
 * so the mutation is persisted to SQLite.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {

        String u = body.get("username");
        String p = body.get("password");

        Map<String, Object> response = new HashMap<>();

        if (adminService.login(u, p)) {
            response.put("success", true);
            response.put("message", "Admin Login Successful!");
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "Invalid Admin credentials.");
        return ResponseEntity.status(401).body(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<Map<String, Object>> assign(@RequestBody Map<String, Object> body) {

        int compId   = Integer.parseInt(body.get("complaintId").toString());
        String staff = body.get("staff").toString();

        Complaint cAssign = complaintService.getComplaintById(compId);

        Map<String, Object> response = new HashMap<>();

        if (cAssign != null) {
            adminService.assignStaff(cAssign, staff);
            complaintService.save(cAssign);
            response.put("success", true);
            response.put("message",
                    "Complaint assigned successfully! Status: " + cAssign.getStatus());
            response.put("complaint", cAssign);
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "Complaint not found.");
        return ResponseEntity.status(404).body(response);
    }

    @PostMapping("/resolve")
    public ResponseEntity<Map<String, Object>> resolve(@RequestBody Map<String, Object> body) {

        int compId        = Integer.parseInt(body.get("complaintId").toString());
        String resolution = body.get("resolution").toString();

        Complaint cResolve = complaintService.getComplaintById(compId);

        Map<String, Object> response = new HashMap<>();

        if (cResolve != null) {
            adminService.resolveComplaint(cResolve, resolution);
            complaintService.save(cResolve);
            response.put("success", true);
            response.put("message",
                    "Complaint resolved successfully! Status: " + cResolve.getStatus());
            response.put("complaint", cResolve);
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "Complaint not found.");
        return ResponseEntity.status(404).body(response);
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> report() {

        Collection<Complaint> all = complaintService.getAllComplaints();
        String reportText = adminService.generateReport(all);

        Map<String, Object> response = new HashMap<>();
        response.put("report", reportText);
        response.put("complaints", all);
        return ResponseEntity.ok(response);
    }
}
