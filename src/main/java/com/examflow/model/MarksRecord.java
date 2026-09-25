package com.examflow.model;

public class MarksRecord {
    private String subjectCode;
    private String subjectName;
    private int credits;
    private double internalMarks; // e.g. Max 30 or 40
    private double externalMarks; // e.g. Max 70 or 60
    private double totalMarks;    // 0 - 100
    private int gradePoint;       // 0 - 10
    private String gradeLetter;   // O, A+, A, B+, B, C, F
    private String status;        // PASS, ARREAR / FAIL

    public MarksRecord() {}

    public MarksRecord(String subjectCode, String subjectName, int credits, double internalMarks,
                       double externalMarks, double totalMarks, int gradePoint, String gradeLetter, String status) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.credits = credits;
        this.internalMarks = internalMarks;
        this.externalMarks = externalMarks;
        this.totalMarks = totalMarks;
        this.gradePoint = gradePoint;
        this.gradeLetter = gradeLetter;
        this.status = status;
    }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public double getInternalMarks() { return internalMarks; }
    public void setInternalMarks(double internalMarks) { this.internalMarks = internalMarks; }

    public double getExternalMarks() { return externalMarks; }
    public void setExternalMarks(double externalMarks) { this.externalMarks = externalMarks; }

    public double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(double totalMarks) { this.totalMarks = totalMarks; }

    public int getGradePoint() { return gradePoint; }
    public void setGradePoint(int gradePoint) { this.gradePoint = gradePoint; }

    public String getGradeLetter() { return gradeLetter; }
    public void setGradeLetter(String gradeLetter) { this.gradeLetter = gradeLetter; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
