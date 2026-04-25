package com.smarthostel.service;

import com.smarthostel.model.Admin;
import com.smarthostel.model.Complaint;
import org.springframework.stereotype.Service;

import java.util.Collection;

/*
 * Same behavior as the original service.AdminService. Every method body
 * is preserved line-for-line:
 *   - login(u,p)               delegates to Admin.login
 *   - assignStaff(c, staff)    sets staff + status "In Progress"
 *   - resolveComplaint(c, r)   sets resolution + status "Resolved"
 *   - generateReport(list)     builds the same StringBuilder report and
 *                              prints it to System.out
 *
 * The only addition is that controllers call complaintService.save(c)
 * after these mutations so the change persists to SQLite; the logic in
 * THIS class is untouched.
 */
@Service
public class AdminService {

    private Admin admin = new Admin();

    public boolean login(String u, String p) {
        return admin.login(u, p);
    }

    public void assignStaff(Complaint c,
                            String staff) {
        c.setAssignedStaff(staff);
        c.setStatus("In Progress");
    }

    public void resolveComplaint(Complaint c,
                                 String resolution) {
        c.setResolution(resolution);
        c.setStatus("Resolved");
    }

    public String generateReport(Collection<Complaint> list) {

        StringBuilder sb = new StringBuilder();
        sb.append("\n--- REPORT ---\n");

        for (Complaint c : list) {
            sb.append("ID: ")
              .append(c.getComplaintId())
              .append(" Status: ")
              .append(c.getStatus())
              .append("\n");
        }

        System.out.println(sb.toString());

        // The original method was void; we additionally return the
        // report text so REST clients can display it. The core logic
        // (System.out.println of the StringBuilder) is unchanged.
        return sb.toString();
    }
}
