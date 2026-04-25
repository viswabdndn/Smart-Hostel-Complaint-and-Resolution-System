package com.smarthostel.service;

import com.smarthostel.model.Complaint;
import com.smarthostel.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/*
 * Same logic as the original service.ComplaintService:
 *  - addComplaint(studentId, type, description) creates a Complaint with
 *    status="Pending" (default from the constructor) and stores it.
 *  - getAllComplaints() returns the full collection.
 *  - getComplaintById(id) returns a single Complaint or null.
 *  - sortByStudentId() returns a new List sorted by studentId, using the
 *    exact anonymous-Comparator pattern from the original code.
 *
 * The in-memory synchronized LinkedHashMap is replaced by a JPA
 * ComplaintRepository backed by SQLite. The `synchronized` keyword on
 * addComplaint is retained so the behavior matches the original.
 */
@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    public synchronized Complaint addComplaint(int studentId,
                                               String type,
                                               String description) {

        // id=0 lets JPA assign the next auto-increment value (IDENTITY),
        // which takes the place of the old `idCounter` field.
        Complaint c = new Complaint(0,
                                    studentId,
                                    type,
                                    description);

        return complaintRepository.save(c);
    }

    public Collection<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(int id) {
        // Original returned null when not found, so preserve that.
        return complaintRepository.findById(id).orElse(null);
    }

    public List<Complaint> sortByStudentId() {

        List<Complaint> list =
                new ArrayList<>(complaintRepository.findAll());

        list.sort(new Comparator<Complaint>() {
            public int compare(Complaint c1,
                               Complaint c2) {
                return c1.getStudentId() -
                       c2.getStudentId();
            }
        });

        return list;
    }

    /*
     * Helper used by controllers (and by AdminService) to persist edits
     * made to a managed Complaint. Behaves like a JPA merge; no existing
     * logic depends on it, it just lets admins save assign/resolve
     * changes back to SQLite.
     */
    public Complaint save(Complaint c) {
        return complaintRepository.save(c);
    }
}
