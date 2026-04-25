package com.smarthostel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Same fields as the original model.Student, now an @Entity so JPA/SQLite
 * can persist it. Field names, getters and setters are all preserved.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    private String name;

    @Column(name = "room_no")
    private String roomNo;

    private String email;
    private String phone;
    private String password;

    public Student() {
        // required by JPA
    }

    public Student(int studentId, String name,
                   String roomNo, String email,
                   String phone, String password) {

        this.studentId = studentId;
        this.name = name;
        this.roomNo = roomNo;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getRoomNo() { return roomNo; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }

    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }
}
