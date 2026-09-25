package com.examflow.model;

public class RevaluationRequest {
    private String id;
    private String rollNo;
    private String studentName;
    private String subjectCode;
    private String subjectName;
    private int semester;
    private String serviceType; // Re-evaluation, Re-totalling, Answer Script Copy
    private double originalMarks;
    private double revisedMarks;
    private String originalGrade;
    private String revisedGrade;
    private String status; // SUBMITTED, UNDER_SCRUTINY, MARKS_REVISED, NO_CHANGE, REJECTED
    private String applicationDate;
    private double feeAmount;
    private String remarks;

    public RevaluationRequest() {}

    public RevaluationRequest(String id, String rollNo, String studentName, String subjectCode,
                              String subjectName, int semester, String serviceType,
                              double originalMarks, double revisedMarks, String originalGrade,
                              String revisedGrade, String status, String applicationDate,
                              double feeAmount, String remarks) {
        this.id = id;
        this.rollNo = rollNo;
        this.studentName = studentName;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
        this.serviceType = serviceType;
        this.originalMarks = originalMarks;
        this.revisedMarks = revisedMarks;
        this.originalGrade = originalGrade;
        this.revisedGrade = revisedGrade;
        this.status = status;
        this.applicationDate = applicationDate;
        this.feeAmount = feeAmount;
        this.remarks = remarks;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public double getOriginalMarks() { return originalMarks; }
    public void setOriginalMarks(double originalMarks) { this.originalMarks = originalMarks; }

    public double getRevisedMarks() { return revisedMarks; }
    public void setRevisedMarks(double revisedMarks) { this.revisedMarks = revisedMarks; }

    public String getOriginalGrade() { return originalGrade; }
    public void setOriginalGrade(String originalGrade) { this.originalGrade = originalGrade; }

    public String getRevisedGrade() { return revisedGrade; }
    public void setRevisedGrade(String revisedGrade) { this.revisedGrade = revisedGrade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApplicationDate() { return applicationDate; }
    public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }

    public double getFeeAmount() { return feeAmount; }
    public void setFeeAmount(double feeAmount) { this.feeAmount = feeAmount; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
