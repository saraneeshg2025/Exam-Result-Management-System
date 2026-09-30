/**
 * ExamFlow Main Application Controller
 * Handles Routing, State, Views, Dynamic Rendering, Printing, and Interactivity
 */

document.addEventListener("DOMContentLoaded", () => {
    App.init();
});

const App = {
    currentRoute: "home",
    currentStudent: null,
    currentResult: null,

    init() {
        this.initTheme();
        this.initRouter();
        this.initEventListeners();
        this.loadInitialData();
    },

    // Theme Management
    initTheme() {
        const savedTheme = localStorage.getItem("examflow_theme") || "light";
        document.documentElement.setAttribute("data-theme", savedTheme);
        this.updateThemeIcon(savedTheme);

        const toggleBtn = document.getElementById("themeToggleBtn");
        if (toggleBtn) {
            toggleBtn.addEventListener("click", () => {
                const current = document.documentElement.getAttribute("data-theme");
                const next = current === "dark" ? "light" : "dark";
                document.documentElement.setAttribute("data-theme", next);
                localStorage.setItem("examflow_theme", next);
                this.updateThemeIcon(next);
            });
        }
    },

    updateThemeIcon(theme) {
        const toggleBtn = document.getElementById("themeToggleBtn");
        if (toggleBtn) {
            toggleBtn.innerHTML = theme === "dark" 
                ? `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/></svg>` 
                : `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>`;
        }
    },

    // Router
    initRouter() {
        window.addEventListener("hashchange", () => this.handleRouting());
        this.handleRouting();
    },

    handleRouting() {
        let hash = window.location.hash.replace("#", "").trim();
        if (!hash) hash = "home";
        this.currentRoute = hash;

        // Switch active nav link
        document.querySelectorAll(".nav-link").forEach(link => {
            const linkRoute = link.getAttribute("data-route");
            if (linkRoute === hash) {
                link.classList.add("active");
            } else {
                link.classList.remove("active");
            }
        });

        // Switch active view
        document.querySelectorAll(".page-view").forEach(view => {
            if (view.id === `view-${hash}`) {
                view.classList.add("active");
            } else {
                view.classList.remove("active");
            }
        });

        window.scrollTo({ top: 0, behavior: "smooth" });

        // Route specific triggers
        if (hash === "schedule") this.renderSchedule();
        if (hash === "analytics") this.renderAnalytics();
        if (hash === "notices") this.renderNotices();
        if (hash === "revaluation") this.renderRevaluations();
        if (hash === "faculty") this.initFacultyPortal();
        if (hash === "admin") this.initAdminPortal();
        if (hash === "student") this.loadStudentDashboard("2024CS101");
    },

    navigate(route) {
        window.location.hash = `#${route}`;
    },

    // Initial Data & Backend Health
    async loadInitialData() {
        const health = await ExamAPI.checkHealth();
        const indicator = document.getElementById("backendStatusIndicator");
        if (indicator) {
            indicator.innerHTML = health.online 
                ? `<span style="color:var(--success); font-weight:700;">● Online (Java 21)</span>` 
                : `<span style="color:var(--warning); font-weight:700;">● Local Client</span>`;
        }

        // Render Home Ticker
        const notices = await ExamAPI.getNotices();
        const ticker = document.getElementById("homeTickerText");
        if (ticker && notices && notices.length) {
            ticker.textContent = notices.map(n => n.title).join("  ★  ");
        }

        // Render Quick Stats on Front Page
        const analytics = await ExamAPI.getAnalytics();
        if (analytics) {
            document.getElementById("statTotalStudents") && (document.getElementById("statTotalStudents").textContent = analytics.totalStudents || "12,450+");
            document.getElementById("statPassRate") && (document.getElementById("statPassRate").textContent = `${analytics.overallPassRate || 98.4}%`);
            document.getElementById("statAvgSgpa") && (document.getElementById("statAvgSgpa").textContent = `${analytics.averageSgpa || 8.42}`);
        }

        // Render Recent Circulars on Home
        this.renderHomeCirculars(notices);
    },

    renderHomeCirculars(notices) {
        const container = document.getElementById("homeCircularsList");
        if (!container || !notices) return;

        container.innerHTML = notices.slice(0, 4).map(n => `
            <div class="card" style="margin-bottom: 12px; padding: 16px; cursor: pointer;" onclick="App.navigate('notices')">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                    <span class="badge ${n.isUrgent ? 'badge-arrear' : 'badge-primary'}">${n.category}</span>
                    <span style="font-size: 12px; color: var(--text-muted);">${n.date}</span>
                </div>
                <h4 style="font-size: 15px; margin-bottom: 4px;">${n.title}</h4>
                <p style="font-size: 13px; color: var(--text-secondary);">${n.content.substring(0, 110)}...</p>
            </div>
        `).join("");
    },

    // Event Listeners
    initEventListeners() {
        // Front Page Instant Search Form
        const frontSearchForm = document.getElementById("frontSearchForm");
        if (frontSearchForm) {
            frontSearchForm.addEventListener("submit", (e) => {
                e.preventDefault();
                const rollNo = document.getElementById("frontRollInput").value.trim();
                const sem = document.getElementById("frontSemSelect").value;
                if (!rollNo) {
                    this.showToast("Please enter a student Roll Number.", "error");
                    return;
                }
                this.navigate("results");
                setTimeout(() => {
                    document.getElementById("resultRollInput").value = rollNo;
                    document.getElementById("resultSemSelect").value = sem;
                    this.searchMarksheet(rollNo, sem);
                }, 100);
            });
        }

        // Result Page Search Form
        const resultSearchForm = document.getElementById("resultSearchForm");
        if (resultSearchForm) {
            resultSearchForm.addEventListener("submit", (e) => {
                e.preventDefault();
                const rollNo = document.getElementById("resultRollInput").value.trim();
                const sem = document.getElementById("resultSemSelect").value;
                this.searchMarksheet(rollNo, sem);
            });
        }

        // Hall Ticket Form
        const hallTicketForm = document.getElementById("hallTicketForm");
        if (hallTicketForm) {
            hallTicketForm.addEventListener("submit", (e) => {
                e.preventDefault();
                const rollNo = document.getElementById("admitRollInput").value.trim();
                this.generateHallTicket(rollNo);
            });
        }

        // Schedule Filters
        const schedDeptFilter = document.getElementById("schedDeptFilter");
        const schedSemFilter = document.getElementById("schedSemFilter");
        const schedTypeFilter = document.getElementById("schedTypeFilter");
        if (schedDeptFilter && schedSemFilter && schedTypeFilter) {
            const trigger = () => this.renderSchedule();
            schedDeptFilter.addEventListener("change", trigger);
            schedSemFilter.addEventListener("change", trigger);
            schedTypeFilter.addEventListener("change", trigger);
        }

        // Revaluation Form
        const revalForm = document.getElementById("revalSubmitForm");
        if (revalForm) {
            revalForm.addEventListener("submit", async (e) => {
                e.preventDefault();
                const rollNo = document.getElementById("revalRollInput").value.trim();
                const subjectCode = document.getElementById("revalSubjectSelect").value;
                const serviceType = document.getElementById("revalTypeSelect").value;
                const sem = 4;

                if (!rollNo || !subjectCode) {
                    this.showToast("Please fill all fields.", "error");
                    return;
                }

                try {
                    const req = await ExamAPI.submitRevaluation({ rollNo, subjectCode, semester: sem, serviceType });
                    this.showToast(`Application ${req.id} submitted successfully! Fee: ₹${req.feeAmount}`, "success");
                    this.renderRevaluations();
                    revalForm.reset();
                } catch (err) {
                    this.showToast("Failed to submit revaluation request: " + err.message, "error");
                }
            });
        }
    },

    // Sample Fill Helper
    fillSampleRoll(rollNo) {
        const frontInput = document.getElementById("frontRollInput");
        const resInput = document.getElementById("resultRollInput");
        const admitInput = document.getElementById("admitRollInput");

        if (frontInput) frontInput.value = rollNo;
        if (resInput) resInput.value = rollNo;
        if (admitInput) admitInput.value = rollNo;

        if (this.currentRoute === "home") {
            document.getElementById("frontSearchForm")?.dispatchEvent(new Event("submit"));
        } else if (this.currentRoute === "results") {
            this.searchMarksheet(rollNo, 4);
        } else if (this.currentRoute === "hallticket") {
            this.generateHallTicket(rollNo);
        }
    },

    // Page 2: Search & Render Marksheet
    async searchMarksheet(rollNo, semester = 4) {
        const container = document.getElementById("marksheetDisplayContainer");
        const loader = document.getElementById("resultLoadingSpinner");
        if (!container) return;

        if (loader) loader.style.display = "block";
        container.innerHTML = "";

        try {
            const result = await ExamAPI.searchResult(rollNo, semester);
            this.currentResult = result;
            if (loader) loader.style.display = "none";
            this.renderMarksheetHTML(result);
            this.showToast(`Marksheet loaded for ${result.studentName} (${result.rollNo})`, "success");
        } catch (err) {
            if (loader) loader.style.display = "none";
            container.innerHTML = `
                <div class="card" style="text-align:center; padding: 48px 24px; border: 1.5px dashed var(--danger-border);">
                    <div style="font-size: 42px; margin-bottom: 12px; color: var(--danger);">⚠️</div>
                    <h3 style="margin-bottom: 8px; color: var(--danger);">Result Lookup Failed</h3>
                    <p style="color: var(--text-secondary); max-width: 480px; margin: 0 auto 20px;">${err.message}</p>
                    <div class="sample-pills">
                        <span>Try testing with available candidates:</span>
                        <button type="button" class="pill-btn" onclick="App.fillSampleRoll('2024CS101')">2024CS101 (Saran V)</button>
                        <button type="button" class="pill-btn" onclick="App.fillSampleRoll('2024CS102')">2024CS102 (Priya S)</button>
                        <button type="button" class="pill-btn" onclick="App.fillSampleRoll('2024CS103')">2024CS103 (Aditya V)</button>
                        <button type="button" class="pill-btn" onclick="App.fillSampleRoll('2024EC302')">2024EC302 (Rahul M)</button>
                    </div>
                </div>
            `;
            this.showToast(err.message, "error");
        }
    },

    renderMarksheetHTML(r) {
        const container = document.getElementById("marksheetDisplayContainer");
        if (!container) return;

        const qrSvg = QRBarcodeUtil.createQRCodeSVG(`EXAMFLOW:VERIF:${r.rollNo}:SEM${r.semester}:${r.digitalVerificationHash}`, 84);

        const rowsHtml = r.subjectMarks.map(m => `
            <tr>
                <td style="font-family: var(--font-mono); font-weight: 600;">${m.subjectCode}</td>
                <td><strong>${m.subjectName}</strong></td>
                <td style="text-align: center;">${m.credits}</td>
                <td style="text-align: center;">${m.internalMarks.toFixed(1)}</td>
                <td style="text-align: center;">${m.externalMarks.toFixed(1)}</td>
                <td style="text-align: center; font-weight: 700;">${m.totalMarks.toFixed(1)}</td>
                <td style="text-align: center; font-weight: 800; color: var(--primary);">${m.gradeLetter}</td>
                <td style="text-align: center;">${m.gradePoint}</td>
                <td style="text-align: center;">
                    <span class="badge ${m.status === 'PASS' ? 'badge-pass' : 'badge-arrear'}">${m.status}</span>
                </td>
            </tr>
        `).join("");

        container.innerHTML = `
            <div class="marksheet-card" id="printableMarksheet">
                <div class="official-stamp-watermark">APEX UNIVERSITY OFFICIAL</div>
                
                <div class="marksheet-university-header">
                    <div class="univ-crest">A</div>
                    <div class="univ-title">APEX INSTITUTE OF TECHNOLOGY & SCIENCE</div>
                    <div class="univ-subtitle">Autonomous Institution | Accredited Grade 'A++' | Controller of Examinations</div>
                    <div style="font-size: 14px; font-weight: 800; margin-top: 10px; color: var(--primary); text-transform: uppercase;">
                        Official Semester Grade Sheet & Statement of Marks
                    </div>
                </div>

                <div class="marksheet-meta-grid">
                    <div class="meta-field">
                        <label>Candidate Name</label>
                        <span>${r.studentName}</span>
                    </div>
                    <div class="meta-field">
                        <label>Roll / Reg Number</label>
                        <span style="font-family: var(--font-mono);">${r.rollNo}</span>
                    </div>
                    <div class="meta-field">
                        <label>Degree & Department</label>
                        <span>${r.degree} - ${r.department}</span>
                    </div>
                    <div class="meta-field">
                        <label>Semester / Session</label>
                        <span>Semester ${r.semester} (${r.academicYear})</span>
                    </div>
                    <div class="meta-field">
                        <label>Publication Date</label>
                        <span>${r.publicationDate}</span>
                    </div>
                    <div class="meta-field">
                        <label>Overall Status</label>
                        <span>
                            <span class="badge ${r.resultStatus === 'PASS' ? 'badge-pass' : 'badge-arrear'}">
                                ${r.resultStatus === 'PASS' ? '✓ PASS / PROMOTED' : '✗ ARREAR(S) PENDING'}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Subject Code</th>
                                <th>Course Title</th>
                                <th style="text-align: center;">Credits</th>
                                <th style="text-align: center;">Internal</th>
                                <th style="text-align: center;">External</th>
                                <th style="text-align: center;">Total</th>
                                <th style="text-align: center;">Grade</th>
                                <th style="text-align: center;">Point</th>
                                <th style="text-align: center;">Result</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${rowsHtml}
                        </tbody>
                    </table>
                </div>

                <div class="marksheet-summary-footer">
                    <div class="score-cards-grid">
                        <div class="score-card">
                            <div class="score-val">${r.sgpa.toFixed(2)}</div>
                            <div class="score-lbl">Semester SGPA</div>
                        </div>
                        <div class="score-card">
                            <div class="score-val">${r.cgpa.toFixed(2)}</div>
                            <div class="score-lbl">Cumulative CGPA</div>
                        </div>
                        <div class="score-card">
                            <div class="score-val">${r.totalCredits}</div>
                            <div class="score-lbl">Total Credits</div>
                        </div>
                        <div class="score-card">
                            <div class="score-val">${r.earnedCredits}</div>
                            <div class="score-lbl">Earned Credits</div>
                        </div>
                    </div>

                    <div class="verification-box">
                        <div class="qr-code-holder">${qrSvg}</div>
                        <div class="qr-info">
                            <h5>Digitally Verified</h5>
                            <p title="${r.digitalVerificationHash}">Hash: ${r.digitalVerificationHash.substring(0, 18)}...</p>
                            <span style="font-size: 10px; color: var(--text-secondary); display: block; margin-top: 4px;">Office of the CoE</span>
                        </div>
                    </div>
                </div>

                <div class="marksheet-actions">
                    <button type="button" class="btn btn-secondary" onclick="App.navigate('revaluation')">
                        Apply Re-evaluation
                    </button>
                    <button type="button" class="btn btn-primary" onclick="App.printDocument()">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 6 2 18 2 18 9"/><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"/><rect x="6" y="14" width="12" height="8"/></svg>
                        Print / Save Official Marksheet (PDF)
                    </button>
                </div>
            </div>
        `;
    },

    // Page 3: Timetable & Schedules
    async renderSchedule() {
        const tableBody = document.getElementById("scheduleTableBody");
        if (!tableBody) return;

        const dept = document.getElementById("schedDeptFilter")?.value || "ALL";
        const sem = document.getElementById("schedSemFilter")?.value || "ALL";
        const type = document.getElementById("schedTypeFilter")?.value || "ALL";

        const list = await ExamAPI.getSchedules(dept, sem, type);
        if (!list || !list.length) {
            tableBody.innerHTML = `<tr><td colspan="7" style="text-align:center; padding:32px; color:var(--text-muted);">No examination schedules match the selected filters.</td></tr>`;
            return;
        }

        tableBody.innerHTML = list.map(s => `
            <tr>
                <td style="font-family: var(--font-mono); font-weight: 700;">${s.subjectCode}</td>
                <td><strong>${s.subjectName}</strong></td>
                <td><span class="badge ${s.session === 'FN' ? 'badge-primary' : 'badge-warning'}">${s.session === 'FN' ? 'Morning (FN)' : 'Afternoon (AN)'}</span></td>
                <td><strong>${s.examDate}</strong></td>
                <td style="font-size: 13px;">${s.timeSlot}</td>
                <td><span class="badge badge-pass">${s.venueHall}</span></td>
                <td><span class="badge badge-secondary">${s.examType}</span></td>
            </tr>
        `).join("");
    },

    // Page 4: Generate Hall Ticket
    async generateHallTicket(rollNo) {
        const container = document.getElementById("hallTicketDisplayContainer");
        if (!container) return;

        try {
            const student = await ExamAPI.getStudent(rollNo);
            if (!student) {
                this.showToast(`Student with Roll No '${rollNo}' not found.`, "error");
                return;
            }

            // Check attendance eligibility (< 75% not allowed)
            if (student.attendancePercentage < 75.0) {
                container.innerHTML = `
                    <div class="card" style="text-align: center; padding: 40px 20px; border: 1.5px dashed var(--danger);">
                        <div style="font-size: 40px; color: var(--danger); margin-bottom: 12px;">🚫</div>
                        <h3 style="color: var(--danger); margin-bottom: 8px;">Hall Ticket Withheld: Attendance Shortage</h3>
                        <p style="color: var(--text-secondary); max-width: 500px; margin: 0 auto 16px;">
                            Candidate <strong>${student.name} (${student.rollNo})</strong> has secured <strong>${student.attendancePercentage}%</strong> attendance, which is below the mandatory 75% university eligibility threshold.
                        </p>
                        <span class="badge badge-arrear">Contact Head of Department for Condonation</span>
                    </div>
                `;
                return;
            }

            const barcodeSvg = QRBarcodeUtil.createBarcodeSVG(student.rollNo, 260, 48);
            const schedules = await ExamAPI.getSchedules(student.department, student.currentSemester, "ALL");

            container.innerHTML = `
                <div class="admit-card" id="printableAdmitCard">
                    <div class="admit-header">
                        <h2>APEX INSTITUTE OF TECHNOLOGY & SCIENCE</h2>
                        <p>OFFICIAL HALL TICKET / ADMIT CARD - SEMESTER IV EXAMINATIONS 2026</p>
                    </div>

                    <div class="admit-body">
                        <div class="admit-info-grid">
                            <div><strong>Candidate Name:</strong> ${student.name}</div>
                            <div><strong>Roll Number:</strong> <span style="font-family:monospace; font-weight:700;">${student.rollNo}</span></div>
                            <div><strong>Degree / Dept:</strong> ${student.degree} - ${student.department}</div>
                            <div><strong>Date of Birth:</strong> ${student.dob}</div>
                            <div><strong>Examination Centre:</strong> Main Academic Block, Campus 1</div>
                            <div><strong>Attendance Status:</strong> <span class="badge badge-pass">${student.attendancePercentage}% (Eligible)</span></div>
                        </div>

                        <div class="admit-photo-box">
                            <div class="candidate-photo">
                                <span style="font-size: 11px; text-align: center;">AFFIX RECENT PHOTO</span>
                            </div>
                            <span style="font-size: 10px; font-weight: 700; color: #475569;">CANDIDATE</span>
                        </div>
                    </div>

                    <div class="table-responsive" style="margin-top: 14px;">
                        <table class="data-table" style="font-size: 12px;">
                            <thead>
                                <tr>
                                    <th>Subject Code</th>
                                    <th>Course Title</th>
                                    <th>Date</th>
                                    <th>Session</th>
                                    <th>Hall</th>
                                    <th style="text-align: center;">Invigilator Sign</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${schedules.map(s => `
                                    <tr>
                                        <td style="font-family:monospace; font-weight:700;">${s.subjectCode}</td>
                                        <td>${s.subjectName}</td>
                                        <td>${s.examDate}</td>
                                        <td>${s.session}</td>
                                        <td>${s.venueHall}</td>
                                        <td style="height: 32px; border: 1px dashed #cbd5e1;"></td>
                                    </tr>
                                `).join("")}
                            </tbody>
                        </table>
                    </div>

                    <div class="admit-barcode">${barcodeSvg}</div>

                    <div class="admit-instructions">
                        <strong>IMPORTANT INSTRUCTIONS TO CANDIDATE:</strong>
                        <ul>
                            <li>Candidates must occupy allotted seats 15 minutes prior to the commencement of the exam.</li>
                            <li>Strictly no mobile phones, digital smart watches, or unauthorized materials inside the hall.</li>
                            <li>This admit card along with official College ID Card must be produced on all days.</li>
                        </ul>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-top: 36px; padding-top: 14px; border-top: 1px solid #cbd5e1; font-size: 12px; font-weight: 700;">
                        <div>Candidate Signature</div>
                        <div>Controller of Examinations</div>
                    </div>
                </div>

                <div style="display: flex; justify-content: flex-end; margin-top: 20px;">
                    <button type="button" class="btn btn-primary" onclick="App.printDocument()">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 6 2 18 2 18 9"/><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"/><rect x="6" y="14" width="12" height="8"/></svg>
                        Print Admit Card (PDF)
                    </button>
                </div>
            `;
            this.showToast(`Admit card generated for ${student.name}`, "success");
        } catch (err) {
            this.showToast(err.message, "error");
        }
    },

    // Page 5: Student Academic Dashboard
    async loadStudentDashboard(rollNo = "2024CS101") {
        const student = await ExamAPI.getStudent(rollNo);
        if (!student) return;

        document.getElementById("dashStudentName") && (document.getElementById("dashStudentName").textContent = student.name);
        document.getElementById("dashStudentRoll") && (document.getElementById("dashStudentRoll").textContent = student.rollNo);
        document.getElementById("dashStudentDept") && (document.getElementById("dashStudentDept").textContent = `${student.degree} - ${student.department}`);
        document.getElementById("dashCgpa") && (document.getElementById("dashCgpa").textContent = student.cgpa.toFixed(2));
        document.getElementById("dashAttendance") && (document.getElementById("dashAttendance").textContent = `${student.attendancePercentage}%`);

        // Load Marksheet preview
        const result = await ExamAPI.searchResult(rollNo, 4);
        if (result) {
            document.getElementById("dashSgpa") && (document.getElementById("dashSgpa").textContent = result.sgpa.toFixed(2));
            document.getElementById("dashCredits") && (document.getElementById("dashCredits").textContent = `${result.earnedCredits} / ${result.totalCredits}`);
            
            const table = document.getElementById("dashMarksSummary");
            if (table) {
                table.innerHTML = result.subjectMarks.map(m => `
                    <tr>
                        <td style="font-family:monospace; font-weight:700;">${m.subjectCode}</td>
                        <td>${m.subjectName}</td>
                        <td style="text-align:center;">${m.credits}</td>
                        <td style="text-align:center; font-weight:700;">${m.totalMarks.toFixed(1)}</td>
                        <td style="text-align:center; font-weight:800; color:var(--primary);">${m.gradeLetter}</td>
                        <td style="text-align:center;"><span class="badge ${m.status === 'PASS' ? 'badge-pass' : 'badge-arrear'}">${m.status}</span></td>
                    </tr>
                `).join("");
            }
        }
    },

    // Page 6: Faculty Grading Portal
    async initFacultyPortal() {
        const studentSelect = document.getElementById("facultyStudentSelect");
        const subjectSelect = document.getElementById("facultySubjectSelect");
        if (!studentSelect || !subjectSelect) return;

        const students = await ExamAPI.getAllStudents();
        const subjects = await ExamAPI.getAllSubjects();

        studentSelect.innerHTML = students.map(s => `<option value="${s.rollNo}">${s.name} (${s.rollNo})</option>`).join("");
        subjectSelect.innerHTML = subjects.map(sub => `<option value="${sub.code}">${sub.code} - ${sub.name}</option>`).join("");

        const recalc = () => {
            const intMarks = parseFloat(document.getElementById("facultyInternalInput")?.value || 0);
            const extMarks = parseFloat(document.getElementById("facultyExternalInput")?.value || 0);
            const total = intMarks + extMarks;
            
            let grade = "F", pt = 0, status = "ARREAR";
            if (total >= 40 && extMarks >= 28) {
                status = "PASS";
                if (total >= 90) { grade = "O"; pt = 10; }
                else if (total >= 80) { grade = "A+"; pt = 9; }
                else if (total >= 70) { grade = "A"; pt = 8; }
                else if (total >= 60) { grade = "B+"; pt = 7; }
                else if (total >= 50) { grade = "B"; pt = 6; }
                else { grade = "C"; pt = 5; }
            }

            document.getElementById("facultyLiveTotal") && (document.getElementById("facultyLiveTotal").textContent = total.toFixed(1));
            document.getElementById("facultyLiveGrade") && (document.getElementById("facultyLiveGrade").textContent = `${grade} (${pt} pts)`);
            document.getElementById("facultyLiveStatus") && (document.getElementById("facultyLiveStatus").innerHTML = `<span class="badge ${status === 'PASS' ? 'badge-pass' : 'badge-arrear'}">${status}</span>`);
        };

        document.getElementById("facultyInternalInput")?.addEventListener("input", recalc);
        document.getElementById("facultyExternalInput")?.addEventListener("input", recalc);
        recalc();

        const submitBtn = document.getElementById("facultySubmitMarksBtn");
        if (submitBtn) {
            submitBtn.onclick = async () => {
                const rollNo = studentSelect.value;
                const subjectCode = subjectSelect.value;
                const internalMarks = parseFloat(document.getElementById("facultyInternalInput").value);
                const externalMarks = parseFloat(document.getElementById("facultyExternalInput").value);

                if (isNaN(internalMarks) || isNaN(externalMarks)) {
                    this.showToast("Please enter valid numerical marks.", "error");
                    return;
                }

                try {
                    await ExamAPI.submitMarks({ rollNo, semester: 4, subjectCode, internalMarks, externalMarks });
                    this.showToast(`Marks successfully updated and submitted for ${rollNo}!`, "success");
                } catch (e) {
                    this.showToast(e.message, "error");
                }
            };
        }
    },

    // Page 7: Controller of Examinations Admin Portal
    async initAdminPortal() {
        const btnPublish = document.getElementById("btnAdminPublish");
        const btnGrace = document.getElementById("btnAdminGrace");

        if (btnPublish) {
            btnPublish.onclick = async () => {
                const res = await ExamAPI.batchPublish(4, true);
                this.showToast(`Batch Published: ${res.publishedCount || 6} Student marksheets are now live!`, "success");
            };
        }

        if (btnGrace) {
            btnGrace.onclick = async () => {
                const res = await ExamAPI.applyGrace(4, 3.0);
                this.showToast(`Moderation Applied: ${res.affectedStudents || 1} student(s) awarded borderline moderation.`, "success");
            };
        }
    },

    // Page 8: Revaluation
    async renderRevaluations() {
        const table = document.getElementById("revalListBody");
        if (!table) return;

        const list = await ExamAPI.getRevaluations();
        table.innerHTML = list.map(r => `
            <tr>
                <td style="font-family:monospace; font-weight:700;">${r.id}</td>
                <td><strong>${r.studentName}</strong> <span style="font-size:11px; color:var(--text-muted);">(${r.rollNo})</span></td>
                <td>${r.subjectCode} - ${r.subjectName}</td>
                <td><span class="badge badge-primary">${r.serviceType}</span></td>
                <td>₹${r.feeAmount}</td>
                <td>
                    <span class="badge ${r.status === 'MARKS_REVISED' ? 'badge-pass' : (r.status === 'UNDER_SCRUTINY' ? 'badge-warning' : 'badge-primary')}">
                        ${r.status}
                    </span>
                </td>
                <td style="font-size:12px; color:var(--text-secondary);">${r.remarks || '-'}</td>
            </tr>
        `).join("");
    },

    // Page 9: Analytics & Merit Rank List
    async renderAnalytics() {
        const stats = await ExamAPI.getAnalytics();
        const toppers = await ExamAPI.getToppers(4);

        document.getElementById("anTotalStudents") && (document.getElementById("anTotalStudents").textContent = stats.totalStudents);
        document.getElementById("anPassCount") && (document.getElementById("anPassCount").textContent = stats.passCount);
        document.getElementById("anArrearCount") && (document.getElementById("anArrearCount").textContent = stats.arrearCount);
        document.getElementById("anOverallRate") && (document.getElementById("anOverallRate").textContent = `${stats.overallPassRate}%`);

        // Toppers Rank List
        const toppersTable = document.getElementById("toppersTableBody");
        if (toppersTable && toppers) {
            toppersTable.innerHTML = toppers.map((t, idx) => `
                <tr>
                    <td style="text-align:center;">
                        <span class="badge ${idx === 0 ? 'badge-warning' : (idx === 1 ? 'badge-primary' : 'badge-pass')}" style="font-size:13px;">
                            ${idx === 0 ? '🥇 Rank 1' : (idx === 1 ? '🥈 Rank 2' : (idx === 2 ? '🥉 Rank 3' : `#${idx + 1}`))}
                        </span>
                    </td>
                    <td><strong>${t.studentName}</strong></td>
                    <td style="font-family:monospace;">${t.rollNo}</td>
                    <td>${t.department}</td>
                    <td style="text-align:center; font-size:16px; font-weight:800; color:var(--primary);">${t.sgpa.toFixed(2)}</td>
                    <td style="text-align:center; font-weight:700;">${t.cgpa.toFixed(2)}</td>
                </tr>
            `).join("");
        }

        // Department Breakdown
        const deptTable = document.getElementById("deptPassTableBody");
        if (deptTable && stats.departmentPassRates) {
            deptTable.innerHTML = stats.departmentPassRates.map(d => `
                <tr>
                    <td><strong>${d.department}</strong></td>
                    <td style="text-align:center;">${d.total}</td>
                    <td style="text-align:center; color:var(--success); font-weight:700;">${d.passed}</td>
                    <td style="text-align:center;">
                        <div style="display:flex; align-items:center; gap:8px; justify-content:center;">
                            <div style="flex:1; max-width:120px; height:8px; background:var(--bg-subtle); border-radius:4px; overflow:hidden;">
                                <div style="width:${d.passPercentage}%; height:100%; background:var(--primary); border-radius:4px;"></div>
                            </div>
                            <strong>${d.passPercentage}%</strong>
                        </div>
                    </td>
                </tr>
            `).join("");
        }
    },

    // Page 10: Notices
    async renderNotices() {
        const container = document.getElementById("fullNoticesList");
        if (!container) return;

        const notices = await ExamAPI.getNotices();
        container.innerHTML = notices.map(n => `
            <div class="card" style="margin-bottom: 18px;">
                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px;">
                    <div>
                        <span class="badge ${n.isUrgent ? 'badge-arrear' : 'badge-primary'}" style="margin-bottom: 6px;">${n.category}</span>
                        <h3 style="font-size: 18px;">${n.title}</h3>
                    </div>
                    <span style="font-size: 13px; font-weight: 600; color: var(--text-muted);">${n.date}</span>
                </div>
                <p style="color: var(--text-secondary); margin-bottom: 14px;">${n.content}</p>
                <div style="display: flex; align-items: center; gap: 12px;">
                    <a href="javascript:void(0)" class="btn btn-secondary" style="font-size: 12px; padding: 6px 12px;" onclick="App.showToast('Downloaded official attachment: ${n.attachmentName}', 'success')">
                        📎 Download Attachment (${n.attachmentName})
                    </a>
                </div>
            </div>
        `).join("");
    },

    // Utilities
    printDocument() {
        window.print();
    },

    showToast(message, type = "info") {
        let container = document.getElementById("toastContainer");
        if (!container) {
            container = document.createElement("div");
            container.id = "toastContainer";
            container.className = "toast-container";
            document.body.appendChild(container);
        }

        const toast = document.createElement("div");
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <span>${type === 'success' ? '✓' : (type === 'error' ? '⚠️' : 'ℹ️')}</span>
            <span>${message}</span>
        `;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = "0";
            toast.style.transform = "translateX(100%)";
            toast.style.transition = "all 200ms ease";
            setTimeout(() => toast.remove(), 200);
        }, 3500);
    }
};
