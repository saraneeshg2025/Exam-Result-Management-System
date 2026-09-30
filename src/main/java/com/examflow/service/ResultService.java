package com.examflow.service;

import com.examflow.model.*;
import com.examflow.repository.DataStore;

import java.time.LocalDate;
import java.util.*;

public class ResultService {
    private final DataStore store = DataStore.getInstance();

    public StudentResult getResult(String rollNo, int semester) {
        if (rollNo == null || rollNo.trim().isEmpty()) return null;
        return store.getResult(rollNo.trim().toUpperCase(), semester);
    }

    public List<StudentResult> getAllResultsForStudent(String rollNo) {
        List<StudentResult> list = new ArrayList<>();
        if (rollNo == null) return list;
        for (StudentResult res : store.getAllResults()) {
            if (res.getRollNo().equalsIgnoreCase(rollNo.trim())) {
                list.add(res);
            }
        }
        list.sort(Comparator.comparingInt(StudentResult::getSemester));
        return list;
    }

    public synchronized StudentResult submitFacultyMarks(String rollNo, int semester, String subjectCode,
                                                         double internal, double external) {
        rollNo = rollNo.trim().toUpperCase();
        Subject sub = store.getSubject(subjectCode);
        Student student = store.getStudent(rollNo);
        if (student == null) {
            throw new IllegalArgumentException("Student with Roll No " + rollNo + " not found.");
        }
        double maxExt = sub != null ? sub.getMaxExternal() : 70.0;
        String subName = sub != null ? sub.getName() : subjectCode;
        int credits = sub != null ? sub.getCredits() : 4;

        StudentResult currentResult = store.getResult(rollNo, semester);
        List<MarksRecord> marksList;
        if (currentResult != null) {
            marksList = new ArrayList<>(currentResult.getSubjectMarks());
        } else {
            marksList = new ArrayList<>();
        }

        // Update or add mark
        boolean updated = false;
        for (int i = 0; i < marksList.size(); i++) {
            MarksRecord m = marksList.get(i);
            if (m.getSubjectCode().equalsIgnoreCase(subjectCode)) {
                double total = internal + external;
                GradeCalculator.GradeInfo g = GradeCalculator.calculateGrade(total, external, maxExt);
                marksList.set(i, new MarksRecord(subjectCode, subName, credits, internal, external, total, g.gradePoint, g.gradeLetter, g.status));
                updated = true;
                break;
            }
        }

        if (!updated) {
            double total = internal + external;
            GradeCalculator.GradeInfo g = GradeCalculator.calculateGrade(total, external, maxExt);
            marksList.add(new MarksRecord(subjectCode, subName, credits, internal, external, total, g.gradePoint, g.gradeLetter, g.status));
        }

        double sgpa = GradeCalculator.computeSGPA(marksList);
        int totalCredits = 0;
        int earnedCredits = 0;
        boolean hasArrear = false;
        for (MarksRecord m : marksList) {
            totalCredits += m.getCredits();
            if ("PASS".equalsIgnoreCase(m.getStatus())) {
                earnedCredits += m.getCredits();
            } else {
                hasArrear = true;
            }
        }

        String pubDate = LocalDate.now().toString();
        String hash = GradeCalculator.generateVerificationHash(rollNo, semester, sgpa, pubDate);
        StudentResult updatedResult = new StudentResult(rollNo, student.getName(), student.getDepartment(),
                student.getDegree(), semester, "2025-2026", sgpa, student.getCgpa(), totalCredits,
                earnedCredits, hasArrear ? "ARREAR" : "PASS", pubDate, hash, true, marksList);

        store.saveResult(updatedResult);
        return updatedResult;
    }

    public synchronized int batchPublishResults(int semester, boolean publishState) {
        int count = 0;
        for (StudentResult res : store.getAllResults()) {
            if (res.getSemester() == semester) {
                res.setPublished(publishState);
                res.setPublicationDate(LocalDate.now().toString());
                count++;
            }
        }
        store.saveAllToDisk();
        return count;
    }

    public synchronized int applyGraceMarks(int semester, double graceMarks) {
        int affectedCount = 0;
        for (StudentResult res : store.getAllResults()) {
            if (res.getSemester() == semester) {
                boolean changed = false;
                for (int i = 0; i < res.getSubjectMarks().size(); i++) {
                    MarksRecord m = res.getSubjectMarks().get(i);
                    // If student failed by graceMarks margin (e.g. 37-39 total marks)
                    if ("ARREAR".equalsIgnoreCase(m.getStatus()) && m.getTotalMarks() >= (40.0 - graceMarks)) {
                        double newExt = m.getExternalMarks() + (40.0 - m.getTotalMarks());
                        double newTotal = m.getInternalMarks() + newExt;
                        Subject s = store.getSubject(m.getSubjectCode());
                        double maxExt = s != null ? s.getMaxExternal() : 70.0;
                        GradeCalculator.GradeInfo g = GradeCalculator.calculateGrade(newTotal, newExt, maxExt);
                        res.getSubjectMarks().set(i, new MarksRecord(m.getSubjectCode(), m.getSubjectName(),
                                m.getCredits(), m.getInternalMarks(), newExt, newTotal, g.gradePoint, g.gradeLetter, g.status));
                        changed = true;
                        affectedCount++;
                    }
                }
                if (changed) {
                    res.setSgpa(GradeCalculator.computeSGPA(res.getSubjectMarks()));
                    boolean stillHasArrear = res.getSubjectMarks().stream().anyMatch(m -> "ARREAR".equalsIgnoreCase(m.getStatus()));
                    res.setResultStatus(stillHasArrear ? "ARREAR" : "PASS");
                }
            }
        }
        store.saveAllToDisk();
        return affectedCount;
    }

    public List<StudentResult> getToppers(int semester) {
        List<StudentResult> list = new ArrayList<>();
        for (StudentResult r : store.getAllResults()) {
            if (r.getSemester() == semester && "PASS".equalsIgnoreCase(r.getResultStatus())) {
                list.add(r);
            }
        }
        list.sort((a, b) -> Double.compare(b.getSgpa(), a.getSgpa()));
        return list;
    }

    public Map<String, Object> getAnalytics() {
        Map<String, Object> analytics = new LinkedHashMap<>();
        Collection<StudentResult> all = store.getAllResults();

        int totalStudents = store.getAllStudents().size();
        int totalResults = all.size();
        int passCount = 0;
        int arrearCount = 0;
        double totalSgpaSum = 0;

        Map<String, int[]> deptStats = new HashMap<>(); // [total, pass]

        for (StudentResult r : all) {
            totalSgpaSum += r.getSgpa();
            deptStats.putIfAbsent(r.getDepartment(), new int[]{0, 0});
            deptStats.get(r.getDepartment())[0]++;

            if ("PASS".equalsIgnoreCase(r.getResultStatus())) {
                passCount++;
                deptStats.get(r.getDepartment())[1]++;
            } else {
                arrearCount++;
            }
        }

        double overallPassRate = totalResults > 0 ? (double) passCount / totalResults * 100.0 : 0.0;
        double avgSgpa = totalResults > 0 ? totalSgpaSum / totalResults : 0.0;

        analytics.put("totalStudents", totalStudents);
        analytics.put("totalResults", totalResults);
        analytics.put("passCount", passCount);
        analytics.put("arrearCount", arrearCount);
        analytics.put("overallPassRate", Math.round(overallPassRate * 10.0) / 10.0);
        analytics.put("averageSgpa", Math.round(avgSgpa * 100.0) / 100.0);

        List<Map<String, Object>> deptList = new ArrayList<>();
        for (Map.Entry<String, int[]> e : deptStats.entrySet()) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("department", e.getKey());
            d.put("total", e.getValue()[0]);
            d.put("passed", e.getValue()[1]);
            double rate = e.getValue()[0] > 0 ? (double) e.getValue()[1] / e.getValue()[0] * 100.0 : 0.0;
            d.put("passPercentage", Math.round(rate * 10.0) / 10.0);
            deptList.add(d);
        }
        analytics.put("departmentPassRates", deptList);
        return analytics;
    }
}
