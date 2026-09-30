package com.examflow.service;

import com.examflow.model.RevaluationRequest;
import com.examflow.model.Student;
import com.examflow.model.Subject;
import com.examflow.repository.DataStore;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RevaluationService {
    private final DataStore store = DataStore.getInstance();
    private final ResultService resultService = new ResultService();

    public List<RevaluationRequest> getAll() {
        return store.getAllRevaluations();
    }

    public RevaluationRequest submitRequest(String rollNo, String subjectCode, int semester, String serviceType) {
        rollNo = rollNo.trim().toUpperCase();
        Student student = store.getStudent(rollNo);
        Subject subject = store.getSubject(subjectCode);

        String stuName = student != null ? student.getName() : "Candidate " + rollNo;
        String subName = subject != null ? subject.getName() : subjectCode;

        double fee = 750.0;
        if ("Re-totalling".equalsIgnoreCase(serviceType)) fee = 300.0;
        else if ("Answer Script Copy".equalsIgnoreCase(serviceType)) fee = 500.0;

        String id = "REV-2026-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        RevaluationRequest req = new RevaluationRequest(id, rollNo, stuName, subjectCode, subName,
                semester, serviceType, 0.0, 0.0, "-", "-", "SUBMITTED",
                LocalDate.now().toString(), fee, "Application registered. Awaiting evaluation committee assignment.");

        store.addRevaluation(req);
        return req;
    }

    public synchronized RevaluationRequest updateStatus(String id, String status, double revisedMarks, String remarks) {
        for (RevaluationRequest req : store.getAllRevaluations()) {
            if (req.getId().equalsIgnoreCase(id)) {
                req.setStatus(status);
                if (revisedMarks > 0) {
                    req.setRevisedMarks(revisedMarks);
                    // Also update student mark
                    try {
                        resultService.submitFacultyMarks(req.getRollNo(), req.getSemester(), req.getSubjectCode(), 30.0, revisedMarks - 30.0);
                    } catch (Exception ignored) {}
                }
                if (remarks != null && !remarks.isEmpty()) {
                    req.setRemarks(remarks);
                }
                store.saveAllToDisk();
                return req;
            }
        }
        return null;
    }
}
