package com.examflow.cli;

import com.examflow.model.*;
import com.examflow.repository.DataStore;
import com.examflow.service.GradeCalculator;
import com.examflow.service.ResultService;

import java.util.List;
import java.util.Scanner;

public class ExamCli {
    public static void main(String[] args) {
        DataStore store = DataStore.getInstance();
        ResultService resultService = new ResultService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=============================================================");
        System.out.println("       APEX EXAM & RESULT MANAGEMENT SYSTEM - CLI CONSOLE    ");
        System.out.println("=============================================================");

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Lookup Student Marksheet (Roll No + Sem)");
            System.out.println("2. View Examination Timetable");
            System.out.println("3. View Semester Merit Toppers");
            System.out.println("4. View University Analytics Summary");
            System.out.println("5. Quick Grade Calculation Simulator");
            System.out.println("6. Exit CLI");
            System.out.print("Enter choice (1-6): ");

            String input = scanner.nextLine().trim();
            if ("6".equals(input) || "exit".equalsIgnoreCase(input)) {
                System.out.println("Exiting Examflow CLI. Goodbye!");
                break;
            }

            switch (input) {
                case "1": {
                    System.out.print("Enter Roll Number (e.g. 2024CS101): ");
                    String roll = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Enter Semester (e.g. 4): ");
                    int sem = 4;
                    try { sem = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception ignored) {}

                    StudentResult r = resultService.getResult(roll, sem);
                    if (r == null) {
                        System.out.println("[!] No result found for " + roll + " (Sem " + sem + ")");
                    } else {
                        System.out.println("\n-----------------------------------------------------------");
                        System.out.println("STUDENT: " + r.getStudentName() + " | ROLL: " + r.getRollNo());
                        System.out.println("DEPT   : " + r.getDepartment() + " | SEMESTER: " + r.getSemester());
                        System.out.println("SGPA   : " + r.getSgpa() + " | STATUS: " + r.getResultStatus());
                        System.out.println("VERIF  : " + r.getDigitalVerificationHash());
                        System.out.println("-----------------------------------------------------------");
                        System.out.printf("%-10s %-32s %-6s %-6s %-6s %-6s %-6s\n", "CODE", "SUBJECT", "CRED", "INT", "EXT", "TOT", "GRADE");
                        for (MarksRecord m : r.getSubjectMarks()) {
                            System.out.printf("%-10s %-32s %-6d %-6.1f %-6.1f %-6.1f %-6s\n",
                                    m.getSubjectCode(),
                                    m.getSubjectName().length() > 30 ? m.getSubjectName().substring(0, 27) + "..." : m.getSubjectName(),
                                    m.getCredits(), m.getInternalMarks(), m.getExternalMarks(), m.getTotalMarks(), m.getGradeLetter());
                        }
                        System.out.println("-----------------------------------------------------------");
                    }
                    break;
                }
                case "2": {
                    List<ExamScheduleItem> list = store.getAllSchedules();
                    System.out.println("\n--- UPCOMING EXAM SCHEDULES ---");
                    for (ExamScheduleItem s : list) {
                        System.out.printf("[%s] %-10s %-30s | Date: %-10s | %-18s | Hall: %s\n",
                                s.getSession(), s.getSubjectCode(), s.getSubjectName(), s.getExamDate(), s.getTimeSlot(), s.getVenueHall());
                    }
                    break;
                }
                case "3": {
                    List<StudentResult> toppers = resultService.getToppers(4);
                    System.out.println("\n--- TOP RANK HOLDERS (SEMESTER 4) ---");
                    int rank = 1;
                    for (StudentResult t : toppers) {
                        System.out.printf("Rank #%d: %-20s (%s) | SGPA: %.2f | Dept: %s\n",
                                rank++, t.getStudentName(), t.getRollNo(), t.getSgpa(), t.getDepartment());
                    }
                    break;
                }
                case "4": {
                    var stats = resultService.getAnalytics();
                    System.out.println("\n--- UNIVERSITY EXAMINATION ANALYTICS ---");
                    System.out.println("Total Registered Students : " + stats.get("totalStudents"));
                    System.out.println("Declared Semester Results : " + stats.get("totalResults"));
                    System.out.println("Overall Pass Percentage   : " + stats.get("overallPassRate") + "%");
                    System.out.println("Average University SGPA   : " + stats.get("averageSgpa"));
                    break;
                }
                case "5": {
                    System.out.print("Enter Internal Marks (0 - 30): ");
                    double in = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Enter External Marks (0 - 70): ");
                    double ex = Double.parseDouble(scanner.nextLine().trim());
                    double tot = in + ex;
                    var g = GradeCalculator.calculateGrade(tot, ex, 70.0);
                    System.out.printf("Total: %.1f/100 -> Grade: %s (Points: %d) | Status: %s\n",
                            tot, g.gradeLetter, g.gradePoint, g.status);
                    break;
                }
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
