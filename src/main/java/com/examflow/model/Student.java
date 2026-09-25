package com.examflow.model;

public class Student {
    private String rollNo;
    private String name;
    private String department;
    private String degree;
    private int currentSemester;
    private String email;
    private String phone;
    private String dob;
    private double attendancePercentage;
    private double cgpa;
    private String avatarUrl;

    public Student() {}

    public Student(String rollNo, String name, String department, String degree, int currentSemester,
                   String email, String phone, String dob, double attendancePercentage, double cgpa, String avatarUrl) {
        this.rollNo = rollNo;
        this.name = name;
        this.department = department;
        this.degree = degree;
        this.currentSemester = currentSemester;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.attendancePercentage = attendancePercentage;
        this.cgpa = cgpa;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public int getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(int currentSemester) { this.currentSemester = currentSemester; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
