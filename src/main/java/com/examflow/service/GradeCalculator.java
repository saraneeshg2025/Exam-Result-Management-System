package com.examflow.service;

import com.examflow.model.MarksRecord;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

public class GradeCalculator {

    public static class GradeInfo {
        public final int gradePoint;
        public final String gradeLetter;
        public final String status;

        public GradeInfo(int gradePoint, String gradeLetter, String status) {
            this.gradePoint = gradePoint;
            this.gradeLetter = gradeLetter;
            this.status = status;
        }
    }

    /**
     * Compute grade point, letter grade, and pass/fail status based on total and external marks.
     */
    public static GradeInfo calculateGrade(double totalMarks, double externalMarks, double maxExternal) {
        // Minimum passing criteria: 40% in external and 40% overall
        double minExternalReq = maxExternal * 0.40;
        if (totalMarks < 40.0 || externalMarks < minExternalReq) {
            return new GradeInfo(0, "F", "ARREAR");
        }

        if (totalMarks >= 90.0) {
            return new GradeInfo(10, "O", "PASS");
        } else if (totalMarks >= 80.0) {
            return new GradeInfo(9, "A+", "PASS");
        } else if (totalMarks >= 70.0) {
            return new GradeInfo(8, "A", "PASS");
        } else if (totalMarks >= 60.0) {
            return new GradeInfo(7, "B+", "PASS");
        } else if (totalMarks >= 50.0) {
            return new GradeInfo(6, "B", "PASS");
        } else {
            return new GradeInfo(5, "C", "PASS");
        }
    }

    /**
     * Compute SGPA (Semester Grade Point Average).
     */
    public static double computeSGPA(List<MarksRecord> marks) {
        if (marks == null || marks.isEmpty()) return 0.0;
        int totalCredits = 0;
        double weightedPoints = 0.0;
        for (MarksRecord m : marks) {
            totalCredits += m.getCredits();
            weightedPoints += (m.getCredits() * m.getGradePoint());
        }
        if (totalCredits == 0) return 0.0;
        double sgpa = weightedPoints / totalCredits;
        return Math.round(sgpa * 100.0) / 100.0;
    }

    /**
     * Generate a cryptographic SHA-256 digital verification hash for the marksheet.
     */
    public static String generateVerificationHash(String rollNo, int semester, double sgpa, String date) {
        try {
            String payload = "EXAMFLOW:" + rollNo + ":SEM" + semester + ":SGPA" + sgpa + ":" + date;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "DIGI-HASH-" + System.currentTimeMillis();
        }
    }
}
