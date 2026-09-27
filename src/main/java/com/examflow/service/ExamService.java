package com.examflow.service;

import com.examflow.model.ExamScheduleItem;
import com.examflow.repository.DataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExamService {
    private final DataStore store = DataStore.getInstance();

    public List<ExamScheduleItem> getSchedules(String department, Integer semester, String examType) {
        List<ExamScheduleItem> all = store.getAllSchedules();
        List<ExamScheduleItem> filtered = new ArrayList<>();
        for (ExamScheduleItem item : all) {
            boolean matchDept = (department == null || department.isEmpty() || "ALL".equalsIgnoreCase(department) || item.getDepartment().equalsIgnoreCase(department));
            boolean matchSem = (semester == null || semester <= 0 || item.getSemester() == semester);
            boolean matchType = (examType == null || examType.isEmpty() || "ALL".equalsIgnoreCase(examType) || item.getExamType().equalsIgnoreCase(examType));

            if (matchDept && matchSem && matchType) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public ExamScheduleItem createSchedule(String subjectCode, String subjectName, String examDate,
                                          String session, String timeSlot, String venueHall,
                                          int semester, String department, String examType) {
        String id = "SCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ExamScheduleItem item = new ExamScheduleItem(id, subjectCode, subjectName, examDate, session, timeSlot, venueHall, semester, department, examType);
        store.addSchedule(item);
        return item;
    }
}
