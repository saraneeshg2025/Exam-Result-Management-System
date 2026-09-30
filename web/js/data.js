/**
 * ExamFlow Data Service & Fallback Storage
 * Automatically synchronizes with Java Backend (port 8085 / 8090)
 */
const FallbackData = {
    students: [
        {
            rollNo: "2024CS101",
            name: "Saran V",
            department: "Computer Science & Engineering",
            degree: "B.Tech",
            currentSemester: 4,
            email: "saran.v@apexuniversity.edu",
            phone: "+91 98401 23456",
            dob: "2004-05-14",
            attendancePercentage: 94.5,
            cgpa: 9.42,
            avatarUrl: "assets/avatar1.png"
        },
        {
            rollNo: "2024CS102",
            name: "Priya Sharma",
            department: "Computer Science & Engineering",
            degree: "B.Tech",
            currentSemester: 4,
            email: "priya.sharma@apexuniversity.edu",
            phone: "+91 98402 34567",
            dob: "2004-08-22",
            attendancePercentage: 89.0,
            cgpa: 8.85,
            avatarUrl: "assets/avatar2.png"
        },
        {
            rollNo: "2024CS103",
            name: "Aditya Verma",
            department: "Computer Science & Engineering",
            degree: "B.Tech",
            currentSemester: 4,
            email: "aditya.v@apexuniversity.edu",
            phone: "+91 98403 45678",
            dob: "2004-03-11",
            attendancePercentage: 78.2,
            cgpa: 7.60,
            avatarUrl: "assets/avatar3.png"
        },
        {
            rollNo: "2024IT201",
            name: "Aarav Patel",
            department: "Information Technology",
            degree: "B.Tech",
            currentSemester: 4,
            email: "aarav.p@apexuniversity.edu",
            phone: "+91 98404 56789",
            dob: "2004-11-05",
            attendancePercentage: 96.0,
            cgpa: 9.15,
            avatarUrl: "assets/avatar4.png"
        },
        {
            rollNo: "2024EC301",
            name: "Kavya Krishnan",
            department: "Electronics & Communication",
            degree: "B.Tech",
            currentSemester: 4,
            email: "kavya.k@apexuniversity.edu",
            phone: "+91 98406 78901",
            dob: "2004-02-28",
            attendancePercentage: 88.0,
            cgpa: 8.54,
            avatarUrl: "assets/avatar6.png"
        },
        {
            rollNo: "2024EC302",
            name: "Rahul Mehta",
            department: "Electronics & Communication",
            degree: "B.Tech",
            currentSemester: 4,
            email: "rahul.m@apexuniversity.edu",
            phone: "+91 98407 89012",
            dob: "2003-12-14",
            attendancePercentage: 72.0,
            cgpa: 6.95,
            avatarUrl: "assets/avatar7.png"
        }
    ],

    subjects: [
        { code: "CS401", name: "Design and Analysis of Algorithms", credits: 4, department: "Computer Science & Engineering", semester: 4, maxInternal: 30, maxExternal: 70 },
        { code: "CS402", name: "Operating Systems & Kernel Architecture", credits: 4, department: "Computer Science & Engineering", semester: 4, maxInternal: 30, maxExternal: 70 },
        { code: "CS403", name: "Database Management Systems", credits: 4, department: "Computer Science & Engineering", semester: 4, maxInternal: 30, maxExternal: 70 },
        { code: "CS404", name: "Computer Networks & Protocols", credits: 3, department: "Computer Science & Engineering", semester: 4, maxInternal: 30, maxExternal: 70 },
        { code: "CS405", name: "Theory of Computation & Automata", credits: 3, department: "Computer Science & Engineering", semester: 4, maxInternal: 30, maxExternal: 70 },
        { code: "CS406", name: "Advanced DBMS & OS Laboratory", credits: 2, department: "Computer Science & Engineering", semester: 4, maxInternal: 40, maxExternal: 60 }
    ],

    schedules: [
        { id: "SCH-01", subjectCode: "CS401", subjectName: "Design and Analysis of Algorithms", examDate: "2026-05-18", session: "FN", timeSlot: "09:30 AM - 12:30 PM", venueHall: "Hall CS-101", semester: 4, department: "Computer Science & Engineering", examType: "Regular" },
        { id: "SCH-02", subjectCode: "CS402", subjectName: "Operating Systems & Kernel Architecture", examDate: "2026-05-20", session: "FN", timeSlot: "09:30 AM - 12:30 PM", venueHall: "Hall CS-102", semester: 4, department: "Computer Science & Engineering", examType: "Regular" },
        { id: "SCH-03", subjectCode: "CS403", subjectName: "Database Management Systems", examDate: "2026-05-22", session: "FN", timeSlot: "09:30 AM - 12:30 PM", venueHall: "Hall CS-101", semester: 4, department: "Computer Science & Engineering", examType: "Regular" },
        { id: "SCH-04", subjectCode: "CS404", subjectName: "Computer Networks & Protocols", examDate: "2026-05-25", session: "FN", timeSlot: "09:30 AM - 12:30 PM", venueHall: "Hall CS-103", semester: 4, department: "Computer Science & Engineering", examType: "Regular" },
        { id: "SCH-05", subjectCode: "CS405", subjectName: "Theory of Computation & Automata", examDate: "2026-05-27", session: "FN", timeSlot: "09:30 AM - 12:30 PM", venueHall: "Hall CS-102", semester: 4, department: "Computer Science & Engineering", examType: "Regular" },
        { id: "SCH-06", subjectCode: "CS406", subjectName: "Advanced DBMS & OS Laboratory", examDate: "2026-05-29", session: "AN", timeSlot: "02:00 PM - 05:00 PM", venueHall: "Software Lab 2", semester: 4, department: "Computer Science & Engineering", examType: "Lab Practical" },
        { id: "SCH-07", subjectCode: "IT401", subjectName: "Web Technologies & Cloud Frameworks", examDate: "2026-05-19", session: "FN", timeSlot: "09:30 AM - 12:30 PM", venueHall: "Hall IT-201", semester: 4, department: "Information Technology", examType: "Regular" },
        { id: "SCH-08", subjectCode: "IT402", subjectName: "Information Security & Cryptography", examDate: "2026-05-21", session: "FN", timeSlot: "09:30 AM - 12:30 PM", venueHall: "Hall IT-202", semester: 4, department: "Information Technology", examType: "Regular" },
        { id: "SCH-09", subjectCode: "EC401", subjectName: "Signals and Systems", examDate: "2026-05-18", session: "AN", timeSlot: "02:00 PM - 05:00 PM", venueHall: "Hall EC-301", semester: 4, department: "Electronics & Communication", examType: "Regular" }
    ],

    results: {
        "2024CS101_4": {
            rollNo: "2024CS101",
            studentName: "Saran V",
            department: "Computer Science & Engineering",
            degree: "B.Tech",
            semester: 4,
            academicYear: "2025-2026",
            sgpa: 9.60,
            cgpa: 9.42,
            totalCredits: 20,
            earnedCredits: 20,
            resultStatus: "PASS",
            publicationDate: "2026-06-12",
            digitalVerificationHash: "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            isPublished: true,
            subjectMarks: [
                { subjectCode: "CS401", subjectName: "Design and Analysis of Algorithms", credits: 4, internalMarks: 29, externalMarks: 66, totalMarks: 95, gradePoint: 10, gradeLetter: "O", status: "PASS" },
                { subjectCode: "CS402", subjectName: "Operating Systems & Kernel Architecture", credits: 4, internalMarks: 28, externalMarks: 64, totalMarks: 92, gradePoint: 10, gradeLetter: "O", status: "PASS" },
                { subjectCode: "CS403", subjectName: "Database Management Systems", credits: 4, internalMarks: 30, externalMarks: 68, totalMarks: 98, gradePoint: 10, gradeLetter: "O", status: "PASS" },
                { subjectCode: "CS404", subjectName: "Computer Networks & Protocols", credits: 3, internalMarks: 27, externalMarks: 63, totalMarks: 90, gradePoint: 10, gradeLetter: "O", status: "PASS" },
                { subjectCode: "CS405", subjectName: "Theory of Computation & Automata", credits: 3, internalMarks: 28, externalMarks: 62, totalMarks: 90, gradePoint: 10, gradeLetter: "O", status: "PASS" },
                { subjectCode: "CS406", subjectName: "Advanced DBMS & OS Laboratory", credits: 2, internalMarks: 39, externalMarks: 58, totalMarks: 97, gradePoint: 10, gradeLetter: "O", status: "PASS" }
            ]
        },
        "2024CS102_4": {
            rollNo: "2024CS102",
            studentName: "Priya Sharma",
            department: "Computer Science & Engineering",
            degree: "B.Tech",
            semester: 4,
            academicYear: "2025-2026",
            sgpa: 8.85,
            cgpa: 8.85,
            totalCredits: 20,
            earnedCredits: 20,
            resultStatus: "PASS",
            publicationDate: "2026-06-12",
            digitalVerificationHash: "5f4dcc3b5aa765d61d8327deb882cf992b96996fb92427ae41e4649b934ca495",
            isPublished: true,
            subjectMarks: [
                { subjectCode: "CS401", subjectName: "Design and Analysis of Algorithms", credits: 4, internalMarks: 27, externalMarks: 60, totalMarks: 87, gradePoint: 9, gradeLetter: "A+", status: "PASS" },
                { subjectCode: "CS402", subjectName: "Operating Systems & Kernel Architecture", credits: 4, internalMarks: 26, externalMarks: 61, totalMarks: 87, gradePoint: 9, gradeLetter: "A+", status: "PASS" },
                { subjectCode: "CS403", subjectName: "Database Management Systems", credits: 4, internalMarks: 28, externalMarks: 63, totalMarks: 91, gradePoint: 10, gradeLetter: "O", status: "PASS" },
                { subjectCode: "CS404", subjectName: "Computer Networks & Protocols", credits: 3, internalMarks: 26, externalMarks: 58, totalMarks: 84, gradePoint: 9, gradeLetter: "A+", status: "PASS" },
                { subjectCode: "CS405", subjectName: "Theory of Computation & Automata", credits: 3, internalMarks: 25, externalMarks: 59, totalMarks: 84, gradePoint: 9, gradeLetter: "A+", status: "PASS" },
                { subjectCode: "CS406", subjectName: "Advanced DBMS & OS Laboratory", credits: 2, internalMarks: 38, externalMarks: 56, totalMarks: 94, gradePoint: 10, gradeLetter: "O", status: "PASS" }
            ]
        },
        "2024CS103_4": {
            rollNo: "2024CS103",
            studentName: "Aditya Verma",
            department: "Computer Science & Engineering",
            degree: "B.Tech",
            semester: 4,
            academicYear: "2025-2026",
            sgpa: 7.60,
            cgpa: 7.60,
            totalCredits: 20,
            earnedCredits: 20,
            resultStatus: "PASS",
            publicationDate: "2026-06-12",
            digitalVerificationHash: "7b8b2b95b8d2b7d4869fb8df20b57e49e9fe580d1969fb92427ae41e4649b934",
            isPublished: true,
            subjectMarks: [
                { subjectCode: "CS401", subjectName: "Design and Analysis of Algorithms", credits: 4, internalMarks: 24, externalMarks: 52, totalMarks: 76, gradePoint: 8, gradeLetter: "A", status: "PASS" },
                { subjectCode: "CS402", subjectName: "Operating Systems & Kernel Architecture", credits: 4, internalMarks: 22, externalMarks: 50, totalMarks: 72, gradePoint: 8, gradeLetter: "A", status: "PASS" },
                { subjectCode: "CS403", subjectName: "Database Management Systems", credits: 4, internalMarks: 25, externalMarks: 55, totalMarks: 80, gradePoint: 9, gradeLetter: "A+", status: "PASS" },
                { subjectCode: "CS404", subjectName: "Computer Networks & Protocols", credits: 3, internalMarks: 19, externalMarks: 32, totalMarks: 51, gradePoint: 6, gradeLetter: "B", status: "PASS" },
                { subjectCode: "CS405", subjectName: "Theory of Computation & Automata", credits: 3, internalMarks: 23, externalMarks: 49, totalMarks: 72, gradePoint: 8, gradeLetter: "A", status: "PASS" },
                { subjectCode: "CS406", subjectName: "Advanced DBMS & OS Laboratory", credits: 2, internalMarks: 34, externalMarks: 50, totalMarks: 84, gradePoint: 9, gradeLetter: "A+", status: "PASS" }
            ]
        },
        "2024EC302_4": {
            rollNo: "2024EC302",
            studentName: "Rahul Mehta",
            department: "Electronics & Communication",
            degree: "B.Tech",
            semester: 4,
            academicYear: "2025-2026",
            sgpa: 6.20,
            cgpa: 6.95,
            totalCredits: 11,
            earnedCredits: 7,
            resultStatus: "ARREAR",
            publicationDate: "2026-06-12",
            digitalVerificationHash: "9a7f34c281e2b95b8d2b7d4869fb8df20b57e49e9fe580d1969fb92427ae41e4",
            isPublished: true,
            subjectMarks: [
                { subjectCode: "EC401", subjectName: "Signals and Systems", credits: 4, internalMarks: 15, externalMarks: 20, totalMarks: 35, gradePoint: 0, gradeLetter: "F", status: "ARREAR" },
                { subjectCode: "EC402", subjectName: "Microprocessors and Microcontrollers", credits: 4, internalMarks: 22, externalMarks: 48, totalMarks: 70, gradePoint: 8, gradeLetter: "A", status: "PASS" },
                { subjectCode: "EC403", subjectName: "Analog and Digital Communication", credits: 3, internalMarks: 23, externalMarks: 49, totalMarks: 72, gradePoint: 8, gradeLetter: "A", status: "PASS" }
            ]
        }
    ],

    revaluations: [
        {
            id: "REV-2026-001",
            rollNo: "2024CS103",
            studentName: "Aditya Verma",
            subjectCode: "CS404",
            subjectName: "Computer Networks & Protocols",
            semester: 4,
            serviceType: "Re-evaluation",
            originalMarks: 51.0,
            revisedMarks: 57.0,
            originalGrade: "B",
            revisedGrade: "B+",
            status: "MARKS_REVISED",
            applicationDate: "2026-06-18",
            feeAmount: 750.0,
            remarks: "Scrutiny completed. +6 marks awarded for Question 4(b) protocol diagrams."
        },
        {
            id: "REV-2026-002",
            rollNo: "2024EC302",
            studentName: "Rahul Mehta",
            subjectCode: "EC401",
            subjectName: "Signals and Systems",
            semester: 4,
            serviceType: "Re-evaluation",
            originalMarks: 35.0,
            revisedMarks: 0.0,
            originalGrade: "F",
            revisedGrade: "F",
            status: "UNDER_SCRUTINY",
            applicationDate: "2026-06-20",
            feeAmount: 750.0,
            remarks: "Answer script forwarded to senior external evaluator panel."
        }
    ],

    notices: [
        {
            id: "NOT-01",
            title: "End Semester Examination (May/June 2026) Official Time Table Released",
            category: "Timetable",
            date: "2026-05-02",
            content: "The Office of the Controller of Examinations announces the schedule for regular and arrear examinations. Students can download hall tickets from the portal.",
            isUrgent: true,
            attachmentName: "Timetable_May_2026.pdf"
        },
        {
            id: "NOT-02",
            title: "Admit Card / Hall Ticket Download Window Active for All Semesters",
            category: "Examination",
            date: "2026-05-05",
            content: "Eligible students with >= 75% attendance are instructed to download and print their official hall ticket. Entry without admit card is strictly prohibited.",
            isUrgent: true,
            attachmentName: "Admit_Card_Instructions.pdf"
        },
        {
            id: "NOT-03",
            title: "Declaration of Semester IV Regular & Arrear Examination Results",
            category: "Result",
            date: "2026-06-12",
            content: "Semester IV results are published online. Grade cards with digital cryptographic verification are available for viewing and printing.",
            isUrgent: false,
            attachmentName: "Result_Notification_Sem4.pdf"
        },
        {
            id: "NOT-04",
            title: "Window Open for Application for Re-evaluation & Answer Script Photocopy",
            category: "Revaluation",
            date: "2026-06-15",
            content: "Candidates desiring to apply for revaluation or photocopy of evaluated answer scripts can submit online within 10 days of result publication.",
            isUrgent: false,
            attachmentName: "Reval_Guidelines_2026.pdf"
        },
        {
            id: "NOT-05",
            title: "Code of Conduct & Standard Operating Procedure on Examination Malpractices",
            category: "Circular",
            date: "2026-05-08",
            content: "Carrying mobile phones, smart watches, notes or programmable devices into examination halls is strictly prohibited under University Regulation 14-B.",
            isUrgent: false,
            attachmentName: "Malpractice_Rules.pdf"
        }
    ]
};

// API Bridge with automatic fallback
const ExamAPI = {
    baseUrl: window.location.origin.includes("http") ? "" : "http://localhost:8085",

    async request(endpoint, options = {}) {
        try {
            const res = await fetch(`${this.baseUrl}${endpoint}`, {
                ...options,
                headers: {
                    "Content-Type": "application/json",
                    ...(options.headers || {})
                }
            });
            if (res.ok) {
                return await res.json();
            }
            throw new Error(`HTTP ${res.status}`);
        } catch (err) {
            console.warn(`[API Fallback] Request to ${endpoint} failed, utilizing local state. Reason:`, err.message);
            return null;
        }
    },

    async checkHealth() {
        const res = await this.request("/api/status");
        return res ? { online: true, ...res } : { online: false, system: "Local Client Mode" };
    },

    async searchResult(rollNo, semester = 4) {
        const res = await this.request(`/api/results/search?rollNo=${encodeURIComponent(rollNo)}&semester=${semester}`);
        if (res) return res;

        // Fallback
        const key = `${rollNo.trim().toUpperCase()}_${semester}`;
        if (FallbackData.results[key]) return FallbackData.results[key];

        // Check if student exists
        const student = FallbackData.students.find(s => s.rollNo.toUpperCase() === rollNo.trim().toUpperCase());
        if (!student) throw new Error(`Student with Roll Number '${rollNo}' not found in registry.`);
        throw new Error(`No published result record found for ${rollNo} in Semester ${semester}.`);
    },

    async getSchedules(department = "", semester = "", examType = "") {
        let qs = [];
        if (department) qs.push(`department=${encodeURIComponent(department)}`);
        if (semester) qs.push(`semester=${semester}`);
        if (examType) qs.push(`examType=${encodeURIComponent(examType)}`);
        const query = qs.length ? `?${qs.join("&")}` : "";

        const res = await this.request(`/api/schedule${query}`);
        if (res) return res;

        return FallbackData.schedules.filter(s => {
            if (department && department !== "ALL" && s.department !== department) return false;
            if (semester && semester !== "ALL" && s.semester != semester) return false;
            if (examType && examType !== "ALL" && s.examType !== examType) return false;
            return true;
        });
    },

    async getStudent(rollNo) {
        const res = await this.request(`/api/students?rollNo=${encodeURIComponent(rollNo)}`);
        if (res) return res;
        return FallbackData.students.find(s => s.rollNo.toUpperCase() === rollNo.trim().toUpperCase()) || null;
    },

    async getAllStudents() {
        const res = await this.request("/api/students");
        return res || FallbackData.students;
    },

    async getAllSubjects() {
        const res = await this.request("/api/subjects");
        return res || FallbackData.subjects;
    },

    async getToppers(semester = 4) {
        const res = await this.request(`/api/toppers?semester=${semester}`);
        if (res) return res;
        const list = Object.values(FallbackData.results).filter(r => r.semester == semester && r.resultStatus === "PASS");
        list.sort((a, b) => b.sgpa - a.sgpa);
        return list;
    },

    async getAnalytics() {
        const res = await this.request("/api/analytics");
        if (res) return res;

        // compute fallback
        const results = Object.values(FallbackData.results);
        const passCount = results.filter(r => r.resultStatus === "PASS").length;
        const total = results.length;
        const rate = total > 0 ? (passCount / total) * 100 : 0;
        const avgSgpa = total > 0 ? (results.reduce((a, b) => a + b.sgpa, 0) / total) : 0;

        return {
            totalStudents: FallbackData.students.length,
            totalResults: total,
            passCount: passCount,
            arrearCount: total - passCount,
            overallPassRate: Math.round(rate * 10) / 10,
            averageSgpa: Math.round(avgSgpa * 100) / 100,
            departmentPassRates: [
                { department: "Computer Science & Engineering", total: 3, passed: 3, passPercentage: 100 },
                { department: "Information Technology", total: 1, passed: 1, passPercentage: 100 },
                { department: "Electronics & Communication", total: 2, passed: 1, passPercentage: 50.0 }
            ]
        };
    },

    async getNotices() {
        const res = await this.request("/api/notices");
        return res || FallbackData.notices;
    },

    async getRevaluations() {
        const res = await this.request("/api/revaluation");
        return res || FallbackData.revaluations;
    },

    async submitRevaluation(data) {
        const res = await this.request("/api/revaluation", {
            method: "POST",
            body: JSON.stringify(data)
        });
        if (res) return res;

        const newReq = {
            id: `REV-2026-${Math.floor(100 + Math.random() * 900)}`,
            ...data,
            studentName: data.studentName || `Candidate ${data.rollNo}`,
            subjectName: data.subjectName || data.subjectCode,
            status: "SUBMITTED",
            applicationDate: new Date().toISOString().split("T")[0],
            feeAmount: data.serviceType === "Re-totalling" ? 300 : (data.serviceType === "Answer Script Copy" ? 500 : 750),
            remarks: "Application received via portal. Verification pending."
        };
        FallbackData.revaluations.unshift(newReq);
        return newReq;
    },

    async submitMarks(data) {
        const res = await this.request("/api/marks/submit", {
            method: "POST",
            body: JSON.stringify(data)
        });
        if (res) return res;

        // Local state update
        const key = `${data.rollNo}_${data.semester}`;
        if (!FallbackData.results[key]) {
            throw new Error(`Student marksheet for ${data.rollNo} Sem ${data.semester} not found.`);
        }
        const r = FallbackData.results[key];
        const m = r.subjectMarks.find(s => s.subjectCode === data.subjectCode);
        if (m) {
            m.internalMarks = data.internalMarks;
            m.externalMarks = data.externalMarks;
            m.totalMarks = data.internalMarks + data.externalMarks;
            m.gradePoint = m.totalMarks >= 90 ? 10 : (m.totalMarks >= 80 ? 9 : (m.totalMarks >= 70 ? 8 : (m.totalMarks >= 60 ? 7 : (m.totalMarks >= 50 ? 6 : (m.totalMarks >= 40 ? 5 : 0)))));
            m.gradeLetter = m.totalMarks >= 90 ? "O" : (m.totalMarks >= 80 ? "A+" : (m.totalMarks >= 70 ? "A" : (m.totalMarks >= 60 ? "B+" : (m.totalMarks >= 50 ? "B" : (m.totalMarks >= 40 ? "C" : "F")))));
            m.status = (m.totalMarks >= 40 && m.externalMarks >= 28) ? "PASS" : "ARREAR";
        }
        return { success: true, result: r };
    },

    async batchPublish(semester, publish) {
        const res = await this.request("/api/results/publish", {
            method: "POST",
            body: JSON.stringify({ semester, publish })
        });
        return res || { success: true, semester, publishedCount: 4 };
    },

    async applyGrace(semester, graceMarks) {
        const res = await this.request("/api/results/grace", {
            method: "POST",
            body: JSON.stringify({ semester, graceMarks })
        });
        return res || { success: true, semester, affectedStudents: 1 };
    }
};
