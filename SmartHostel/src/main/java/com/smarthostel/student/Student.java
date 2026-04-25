package com.smarthostel.student;

/*
 * Preserved from the original project's root-level Student.java
 * (package `student`). It's unrelated to the hostel domain models,
 * but kept here verbatim so no logic from the source is lost.
 */
public class Student {
    protected int rollNumber;
    protected String name;
    protected int marks;

    // Constructor
    public Student(int rollNumber, String name, int marks) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.marks = marks;
    }

    // Method to calculate grade
    public String calculateGrade() {
        if (marks >= 90)
            return "A+";
        else if (marks >= 75)
            return "A";
        else if (marks >= 60)
            return "B";
        else if (marks >= 50)
            return "C";
        else
            return "Fail";
    }

    // Method to display student details
    public void display() {
        System.out.println("Roll Number: " + rollNumber);
        System.out.println("Name: " + name);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + calculateGrade());
    }
}
