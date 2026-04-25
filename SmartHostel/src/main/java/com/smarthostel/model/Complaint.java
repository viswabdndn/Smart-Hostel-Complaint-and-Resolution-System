package com.smarthostel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/*
 * Same fields, same defaults, same getters/setters as the original
 * model.Complaint. Now an @Entity so it persists to SQLite via JPA.
 *
 * Defaults preserved from the original constructor:
 *   status        = "Pending"
 *   resolution    = ""
 *   assignedStaff = ""
 *   date          = LocalDate.now()
 */
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complaintId;

    @Column(name = "student_id")
    private int studentId;

    private String type;
    private String description;
    private String status;
    private String resolution;

    @Column(name = "assigned_staff")
    private String assignedStaff;

    private LocalDate date;

    public Complaint() {
        // required by JPA
    }

    public Complaint(int complaintId, int studentId,
                     String type, String description) {

        this.complaintId = complaintId;
        this.studentId = studentId;
        this.type = type;
        this.description = description;
        this.status = "Pending";
        this.resolution = "";
        this.assignedStaff = "";
        this.date = LocalDate.now();
    }

    public int getComplaintId() { return complaintId; }
    public int getStudentId() { return studentId; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getResolution() { return resolution; }
    public String getAssignedStaff() { return assignedStaff; }
    public LocalDate getDate() { return date; }

    public void setComplaintId(int complaintId) { this.complaintId = complaintId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public void setAssignedStaff(String staff) { this.assignedStaff = staff; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
}
