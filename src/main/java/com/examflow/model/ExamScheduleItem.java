package com.examflow.model;

public class ExamScheduleItem {
    private String id;
    private String subjectCode;
    private String subjectName;
    private String examDate;
    private String session; // FN (Morning: 09:30 AM - 12:30 PM) or AN (Afternoon: 02:00 PM - 05:00 PM)
    private String timeSlot;
    private String venueHall;
    private int semester;
    private String department;
    private String examType; // Regular, Supplementary, Lab Practical

    public ExamScheduleItem() {}

    public ExamScheduleItem(String id, String subjectCode, String subjectName, String examDate,
                            String session, String timeSlot, String venueHall, int semester,
                            String department, String examType) {
        this.id = id;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.examDate = examDate;
        this.session = session;
        this.timeSlot = timeSlot;
        this.venueHall = venueHall;
        this.semester = semester;
        this.department = department;
        this.examType = examType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getExamDate() { return examDate; }
    public void setExamDate(String examDate) { this.examDate = examDate; }

    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getVenueHall() { return venueHall; }
    public void setVenueHall(String venueHall) { this.venueHall = venueHall; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
}
