package com.examflow.repository;

import com.examflow.model.*;
import com.examflow.service.GradeCalculator;
import com.examflow.util.SimpleJson;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private static DataStore instance;

    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, Subject> subjects = new ConcurrentHashMap<>();
    private final List<ExamScheduleItem> schedules = new ArrayList<>();
    // Key: rollNo + "_" + semester
    private final Map<String, StudentResult> results = new ConcurrentHashMap<>();
    private final List<RevaluationRequest> revaluations = new ArrayList<>();
    private final List<Notice> notices = new ArrayList<>();

    private final String dataDir = "data";

    private DataStore() {
        seedInitialData();
        saveAllToDisk();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seedInitialData() {
        // 1. Seed Students
        addStudent(new Student("2024CS101", "Saran V", "Computer Science & Engineering", "B.Tech", 4,
                "saran.v@apexuniversity.edu", "+91 98401 23456", "2004-05-14", 94.5, 9.42, "assets/avatar1.png"));
        addStudent(new Student("2024CS102", "Priya Sharma", "Computer Science & Engineering", "B.Tech", 4,
                "priya.sharma@apexuniversity.edu", "+91 98402 34567", "2004-08-22", 89.0, 8.85, "assets/avatar2.png"));
        addStudent(new Student("2024CS103", "Aditya Verma", "Computer Science & Engineering", "B.Tech", 4,
                "aditya.v@apexuniversity.edu", "+91 98403 45678", "2004-03-11", 78.2, 7.60, "assets/avatar3.png"));
        addStudent(new Student("2024IT201", "Aarav Patel", "Information Technology", "B.Tech", 4,
                "aarav.p@apexuniversity.edu", "+91 98404 56789", "2004-11-05", 96.0, 9.15, "assets/avatar4.png"));
        addStudent(new Student("2024IT202", "Sneha Nair", "Information Technology", "B.Tech", 4,
                "sneha.n@apexuniversity.edu", "+91 98405 67890", "2004-07-19", 91.5, 8.92, "assets/avatar5.png"));
        addStudent(new Student("2024EC301", "Kavya Krishnan", "Electronics & Communication", "B.Tech", 4,
                "kavya.k@apexuniversity.edu", "+91 98406 78901", "2004-02-28", 88.0, 8.54, "assets/avatar6.png"));
        addStudent(new Student("2024EC302", "Rahul Mehta", "Electronics & Communication", "B.Tech", 4,
                "rahul.m@apexuniversity.edu", "+91 98407 89012", "2003-12-14", 72.0, 6.95, "assets/avatar7.png"));
        addStudent(new Student("2024ME401", "Vikram Singh", "Mechanical Engineering", "B.Tech", 4,
                "vikram.s@apexuniversity.edu", "+91 98408 90123", "2004-09-30", 84.0, 7.85, "assets/avatar8.png"));

        // 2. Seed Subjects
        addSubject(new Subject("CS401", "Design and Analysis of Algorithms", 4, "Computer Science & Engineering", 4, 30, 70, false));
        addSubject(new Subject("CS402", "Operating Systems & Kernel Architecture", 4, "Computer Science & Engineering", 4, 30, 70, false));
        addSubject(new Subject("CS403", "Database Management Systems", 4, "Computer Science & Engineering", 4, 30, 70, false));
        addSubject(new Subject("CS404", "Computer Networks & Protocols", 3, "Computer Science & Engineering", 4, 30, 70, false));
        addSubject(new Subject("CS405", "Theory of Computation & Automata", 3, "Computer Science & Engineering", 4, 30, 70, false));
        addSubject(new Subject("CS406", "Advanced DBMS & OS Laboratory", 2, "Computer Science & Engineering", 4, 40, 60, true));

        addSubject(new Subject("IT401", "Web Technologies & Cloud Frameworks", 4, "Information Technology", 4, 30, 70, false));
        addSubject(new Subject("IT402", "Information Security & Cryptography", 4, "Information Technology", 4, 30, 70, false));
        addSubject(new Subject("IT403", "Data Warehousing & Mining", 3, "Information Technology", 4, 30, 70, false));
        addSubject(new Subject("IT404", "Full Stack Development Lab", 2, "Information Technology", 4, 40, 60, true));

        addSubject(new Subject("EC401", "Signals and Systems", 4, "Electronics & Communication", 4, 30, 70, false));
        addSubject(new Subject("EC402", "Microprocessors and Microcontrollers", 4, "Electronics & Communication", 4, 30, 70, false));
        addSubject(new Subject("EC403", "Analog and Digital Communication", 3, "Electronics & Communication", 4, 30, 70, false));

        addSubject(new Subject("ME401", "Kinematics of Machinery", 4, "Mechanical Engineering", 4, 30, 70, false));
        addSubject(new Subject("ME402", "Applied Thermal Engineering", 4, "Mechanical Engineering", 4, 30, 70, false));
        addSubject(new Subject("ME403", "Fluid Mechanics & Turbo Machinery", 4, "Mechanical Engineering", 4, 30, 70, false));

        // 3. Seed Exam Schedules
        schedules.add(new ExamScheduleItem("SCH-01", "CS401", "Design and Analysis of Algorithms", "2026-05-18", "FN", "09:30 AM - 12:30 PM", "Hall CS-101", 4, "Computer Science & Engineering", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-02", "CS402", "Operating Systems & Kernel Architecture", "2026-05-20", "FN", "09:30 AM - 12:30 PM", "Hall CS-102", 4, "Computer Science & Engineering", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-03", "CS403", "Database Management Systems", "2026-05-22", "FN", "09:30 AM - 12:30 PM", "Hall CS-101", 4, "Computer Science & Engineering", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-04", "CS404", "Computer Networks & Protocols", "2026-05-25", "FN", "09:30 AM - 12:30 PM", "Hall CS-103", 4, "Computer Science & Engineering", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-05", "CS405", "Theory of Computation & Automata", "2026-05-27", "FN", "09:30 AM - 12:30 PM", "Hall CS-102", 4, "Computer Science & Engineering", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-06", "CS406", "Advanced DBMS & OS Laboratory", "2026-05-29", "AN", "02:00 PM - 05:00 PM", "Software Lab 2", 4, "Computer Science & Engineering", "Lab Practical"));

        schedules.add(new ExamScheduleItem("SCH-07", "IT401", "Web Technologies & Cloud Frameworks", "2026-05-19", "FN", "09:30 AM - 12:30 PM", "Hall IT-201", 4, "Information Technology", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-08", "IT402", "Information Security & Cryptography", "2026-05-21", "FN", "09:30 AM - 12:30 PM", "Hall IT-202", 4, "Information Technology", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-09", "IT403", "Data Warehousing & Mining", "2026-05-23", "FN", "09:30 AM - 12:30 PM", "Hall IT-201", 4, "Information Technology", "Regular"));

        schedules.add(new ExamScheduleItem("SCH-10", "EC401", "Signals and Systems", "2026-05-18", "AN", "02:00 PM - 05:00 PM", "Hall EC-301", 4, "Electronics & Communication", "Regular"));
        schedules.add(new ExamScheduleItem("SCH-11", "EC402", "Microprocessors and Microcontrollers", "2026-05-21", "AN", "02:00 PM - 05:00 PM", "Hall EC-302", 4, "Electronics & Communication", "Regular"));

        // 4. Seed Results (Semester 4 for students)
        // Saran V (2024CS101) - Outstanding results
        List<MarksRecord> saranMarks = new ArrayList<>();
        saranMarks.add(createMarksRecord("CS401", "Design and Analysis of Algorithms", 4, 29, 66, 70));
        saranMarks.add(createMarksRecord("CS402", "Operating Systems & Kernel Architecture", 4, 28, 64, 70));
        saranMarks.add(createMarksRecord("CS403", "Database Management Systems", 4, 30, 68, 70));
        saranMarks.add(createMarksRecord("CS404", "Computer Networks & Protocols", 3, 27, 63, 70));
        saranMarks.add(createMarksRecord("CS405", "Theory of Computation & Automata", 3, 28, 62, 70));
        saranMarks.add(createMarksRecord("CS406", "Advanced DBMS & OS Laboratory", 2, 39, 58, 60));
        saveComputedResult("2024CS101", 4, saranMarks, "2025-2026", "2026-06-12");

        // Priya Sharma (2024CS102)
        List<MarksRecord> priyaMarks = new ArrayList<>();
        priyaMarks.add(createMarksRecord("CS401", "Design and Analysis of Algorithms", 4, 27, 60, 70));
        priyaMarks.add(createMarksRecord("CS402", "Operating Systems & Kernel Architecture", 4, 26, 61, 70));
        priyaMarks.add(createMarksRecord("CS403", "Database Management Systems", 4, 28, 63, 70));
        priyaMarks.add(createMarksRecord("CS404", "Computer Networks & Protocols", 3, 26, 58, 70));
        priyaMarks.add(createMarksRecord("CS405", "Theory of Computation & Automata", 3, 25, 59, 70));
        priyaMarks.add(createMarksRecord("CS406", "Advanced DBMS & OS Laboratory", 2, 38, 56, 60));
        saveComputedResult("2024CS102", 4, priyaMarks, "2025-2026", "2026-06-12");

        // Aditya Verma (2024CS103) - Passed with lower grades in Networks
        List<MarksRecord> adityaMarks = new ArrayList<>();
        adityaMarks.add(createMarksRecord("CS401", "Design and Analysis of Algorithms", 4, 24, 52, 70));
        adityaMarks.add(createMarksRecord("CS402", "Operating Systems & Kernel Architecture", 4, 22, 50, 70));
        adityaMarks.add(createMarksRecord("CS403", "Database Management Systems", 4, 25, 55, 70));
        adityaMarks.add(createMarksRecord("CS404", "Computer Networks & Protocols", 3, 19, 32, 70)); // 51 total -> Grade B
        adityaMarks.add(createMarksRecord("CS405", "Theory of Computation & Automata", 3, 23, 49, 70));
        adityaMarks.add(createMarksRecord("CS406", "Advanced DBMS & OS Laboratory", 2, 34, 50, 60));
        saveComputedResult("2024CS103", 4, adityaMarks, "2025-2026", "2026-06-12");

        // Aarav Patel (2024IT201)
        List<MarksRecord> aaravMarks = new ArrayList<>();
        aaravMarks.add(createMarksRecord("IT401", "Web Technologies & Cloud Frameworks", 4, 28, 65, 70));
        aaravMarks.add(createMarksRecord("IT402", "Information Security & Cryptography", 4, 27, 63, 70));
        aaravMarks.add(createMarksRecord("IT403", "Data Warehousing & Mining", 3, 26, 61, 70));
        aaravMarks.add(createMarksRecord("IT404", "Full Stack Development Lab", 2, 38, 57, 60));
        saveComputedResult("2024IT201", 4, aaravMarks, "2025-2026", "2026-06-12");

        // Kavya Krishnan (2024EC301)
        List<MarksRecord> kavyaMarks = new ArrayList<>();
        kavyaMarks.add(createMarksRecord("EC401", "Signals and Systems", 4, 26, 59, 70));
        kavyaMarks.add(createMarksRecord("EC402", "Microprocessors and Microcontrollers", 4, 27, 60, 70));
        kavyaMarks.add(createMarksRecord("EC403", "Analog and Digital Communication", 3, 25, 58, 70));
        saveComputedResult("2024EC301", 4, kavyaMarks, "2025-2026", "2026-06-12");

        // Rahul Mehta (2024EC302) - Had 1 Arrear in Signals and Systems
        List<MarksRecord> rahulMarks = new ArrayList<>();
        rahulMarks.add(createMarksRecord("EC401", "Signals and Systems", 4, 15, 20, 70)); // Arrear (35 total)
        rahulMarks.add(createMarksRecord("EC402", "Microprocessors and Microcontrollers", 4, 22, 48, 70));
        rahulMarks.add(createMarksRecord("EC403", "Analog and Digital Communication", 3, 23, 49, 70));
        saveComputedResult("2024EC302", 4, rahulMarks, "2025-2026", "2026-06-12");

        // 5. Seed Revaluation
        revaluations.add(new RevaluationRequest("REV-2026-001", "2024CS103", "Aditya Verma", "CS404",
                "Computer Networks & Protocols", 4, "Re-evaluation", 51.0, 57.0, "B", "B+", "MARKS_REVISED",
                "2026-06-18", 750.0, "Scrutiny completed. +6 marks awarded for Question 4(b)."));
        revaluations.add(new RevaluationRequest("REV-2026-002", "2024EC302", "Rahul Mehta", "EC401",
                "Signals and Systems", 4, "Re-evaluation", 35.0, 0.0, "F", "F", "UNDER_SCRUTINY",
                "2026-06-20", 750.0, "Application received. Assigned to external senior examiner."));

        // 6. Seed Notices
        notices.add(new Notice("NOT-01", "End Semester Examination (May/June 2026) Official Time Table Released",
                "Timetable", "2026-05-02",
                "The Office of the Controller of Examinations announces the schedule for regular and arrear examinations. Students can download admit cards from the portal.",
                true, "Timetable_May_2026.pdf"));
        notices.add(new Notice("NOT-02", "Admit Card / Hall Ticket Download Window Active for All Semesters",
                "Examination", "2026-05-05",
                "Eligible students with >= 75% attendance are instructed to download and print their official hall ticket. No student will be allowed without hall ticket and ID.",
                true, "Admit_Card_Instructions.pdf"));
        notices.add(new Notice("NOT-03", "Declaration of Semester IV Regular & Arrear Examination Results",
                "Result", "2026-06-12",
                "Semester IV results are published online. Grade cards with digital cryptographic verification are available for viewing and printing.",
                false, "Result_Notification_Sem4.pdf"));
        notices.add(new Notice("NOT-04", "Window Open for Application for Re-evaluation & Answer Script Photocopy",
                "Revaluation", "2026-06-15",
                "Candidates desiring to apply for revaluation or photocopy of evaluated answer scripts can submit online within 10 days of result publication.",
                false, "Reval_Guidelines_2026.pdf"));
        notices.add(new Notice("NOT-05", "Code of Conduct & Standard Operating Procedure on Examination Malpractices",
                "Circular", "2026-05-08",
                "Carrying mobile phones, smart watches, notes or programmable devices into examination halls is strictly prohibited under University Regulation 14-B.",
                false, "Malpractice_Rules.pdf"));
    }

    private MarksRecord createMarksRecord(String code, String name, int credits, double internal, double external, double maxExt) {
        double total = internal + external;
        GradeCalculator.GradeInfo g = GradeCalculator.calculateGrade(total, external, maxExt);
        return new MarksRecord(code, name, credits, internal, external, total, g.gradePoint, g.gradeLetter, g.status);
    }

    private void saveComputedResult(String rollNo, int semester, List<MarksRecord> marks, String academicYear, String pubDate) {
        Student s = students.get(rollNo);
        if (s == null) return;
        double sgpa = GradeCalculator.computeSGPA(marks);
        int totalCredits = 0;
        int earnedCredits = 0;
        boolean hasFail = false;
        for (MarksRecord m : marks) {
            totalCredits += m.getCredits();
            if ("PASS".equalsIgnoreCase(m.getStatus())) {
                earnedCredits += m.getCredits();
            } else {
                hasFail = true;
            }
        }
        String status = hasFail ? "ARREAR" : "PASS";
        String hash = GradeCalculator.generateVerificationHash(rollNo, semester, sgpa, pubDate);

        StudentResult result = new StudentResult(rollNo, s.getName(), s.getDepartment(), s.getDegree(),
                semester, academicYear, sgpa, s.getCgpa(), totalCredits, earnedCredits, status, pubDate, hash, true, marks);
        results.put(rollNo + "_" + semester, result);
    }

    public synchronized void saveAllToDisk() {
        try {
            File dir = new File(dataDir);
            if (!dir.exists()) dir.mkdirs();

            Files.writeString(Paths.get(dataDir, "students.json"), SimpleJson.toJson(new ArrayList<>(students.values())), StandardCharsets.UTF_8);
            Files.writeString(Paths.get(dataDir, "subjects.json"), SimpleJson.toJson(new ArrayList<>(subjects.values())), StandardCharsets.UTF_8);
            Files.writeString(Paths.get(dataDir, "schedules.json"), SimpleJson.toJson(schedules), StandardCharsets.UTF_8);
            Files.writeString(Paths.get(dataDir, "results.json"), SimpleJson.toJson(new ArrayList<>(results.values())), StandardCharsets.UTF_8);
            Files.writeString(Paths.get(dataDir, "revaluations.json"), SimpleJson.toJson(revaluations), StandardCharsets.UTF_8);
            Files.writeString(Paths.get(dataDir, "notices.json"), SimpleJson.toJson(notices), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Warning: Unable to persist data to disk: " + e.getMessage());
        }
    }

    // Accessors
    public void addStudent(Student s) { students.put(s.getRollNo(), s); }
    public Student getStudent(String rollNo) { return students.get(rollNo); }
    public Collection<Student> getAllStudents() { return students.values(); }

    public void addSubject(Subject sub) { subjects.put(sub.getCode(), sub); }
    public Subject getSubject(String code) { return subjects.get(code); }
    public Collection<Subject> getAllSubjects() { return subjects.values(); }

    public List<ExamScheduleItem> getAllSchedules() { return schedules; }
    public void addSchedule(ExamScheduleItem item) { schedules.add(item); saveAllToDisk(); }

    public StudentResult getResult(String rollNo, int semester) {
        return results.get(rollNo + "_" + semester);
    }
    public Collection<StudentResult> getAllResults() { return results.values(); }
    public void saveResult(StudentResult result) {
        results.put(result.getRollNo() + "_" + result.getSemester(), result);
        saveAllToDisk();
    }

    public List<RevaluationRequest> getAllRevaluations() { return revaluations; }
    public void addRevaluation(RevaluationRequest req) { revaluations.add(0, req); saveAllToDisk(); }

    public List<Notice> getAllNotices() { return notices; }
    public void addNotice(Notice notice) { notices.add(0, notice); saveAllToDisk(); }
}
