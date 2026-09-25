package com.examflow.model;

import java.util.ArrayList;
import java.util.List;

public class StudentResult {
    private String rollNo;
    private String studentName;
    private String department;
    private String degree;
    private int semester;
    private String academicYear;
    private double sgpa;
    private double cgpa;
    private int totalCredits;
    private int earnedCredits;
    private String resultStatus; // PASS, WITHHELD, ARREAR / FAIL
    private String publicationDate;
    private String digitalVerificationHash;
    private boolean isPublished;
    private List<MarksRecord> subjectMarks = new ArrayList<>();

    public StudentResult() {}

    public StudentResult(String rollNo, String studentName, String department, String degree,
                         int semester, String academicYear, double sgpa, double cgpa,
                         int totalCredits, int earnedCredits, String resultStatus,
                         String publicationDate, String digitalVerificationHash,
                         boolean isPublished, List<MarksRecord> subjectMarks) {
        this.rollNo = rollNo;
        this.studentName = studentName;
        this.department = department;
        this.degree = degree;
        this.semester = semester;
        this.academicYear = academicYear;
        this.sgpa = sgpa;
        this.cgpa = cgpa;
        this.totalCredits = totalCredits;
        this.earnedCredits = earnedCredits;
        this.resultStatus = resultStatus;
        this.publicationDate = publicationDate;
        this.digitalVerificationHash = digitalVerificationHash;
        this.isPublished = isPublished;
        this.subjectMarks = subjectMarks != null ? subjectMarks : new ArrayList<>();
    }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public double getSgpa() { return sgpa; }
    public void setSgpa(double sgpa) { this.sgpa = sgpa; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public int getTotalCredits() { return totalCredits; }
    public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }

    public int getEarnedCredits() { return earnedCredits; }
    public void setEarnedCredits(int earnedCredits) { this.earnedCredits = earnedCredits; }

    public String getResultStatus() { return resultStatus; }
    public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }

    public String getPublicationDate() { return publicationDate; }
    public void setPublicationDate(String publicationDate) { this.publicationDate = publicationDate; }

    public String getDigitalVerificationHash() { return digitalVerificationHash; }
    public void setDigitalVerificationHash(String digitalVerificationHash) { this.digitalVerificationHash = digitalVerificationHash; }

    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean published) { isPublished = published; }

    public List<MarksRecord> getSubjectMarks() { return subjectMarks; }
    public void setSubjectMarks(List<MarksRecord> subjectMarks) { this.subjectMarks = subjectMarks; }
}
