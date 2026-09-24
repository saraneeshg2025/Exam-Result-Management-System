# Apex ExamFlow - Exam & Result Management System

A state-of-the-art **Academic Examination & Result Management System** built with **Java 21** and a responsive modern web portal. Featuring a clean, simple front page design, authenticated marksheet generation, digital QR verification, Choice Based Credit System (CBCS) 10-point GPA calculation, exam timetables, admit cards/hall tickets, faculty grading, re-evaluation scrutiny, and university analytics.

---

## 🌟 Key Features

### 1. Simple, Clean Front Page
- **Quick Result Finder**: Enter Roll Number (e.g. `2024CS101`) to instantly lookup verified marksheets.
- **Pre-filled Sample Candidate Pills**: Test the system with 1-click presets.
- **Portals Hub**: Instant navigation to Student, Faculty, and Admin suites.
- **Live Notifications Ticker**: Urgent exam announcements and timetable releases.
- **Key Academic Figures**: Real-time stats on registered students, pass percentages, and average SGPA.

### 2. Multi-Page Academic Suite (10 Dedicated Views)
1. **Home Page**: Clean, uncluttered landing interface with quick search and portal cards.
2. **Result & Marksheet Portal**:
   - Official semester grade card adhering to UGC / AICTE Choice Based Credit System (CBCS).
   - Letter grades (`O`, `A+`, `A`, `B+`, `B`, `C`, `F`) and credit-weighted SGPA/CGPA.
   - Dynamic SVG QR code linking to digital verification hash.
   - Print & PDF download formatting.
3. **Exam Timetable & Schedule Explorer**:
   - Filterable by department (CSE, IT, ECE, MECH), semester, and exam type.
   - Session slots (FN 09:30-12:30, AN 02:00-05:00) and venue halls.
4. **Hall Ticket / Admit Card Portal**:
   - Candidate verification and mandatory 75% attendance threshold check.
   - Generates official admit card with barcode, candidate photo placeholder, and registered courses.
5. **Student Academic Dashboard**:
   - Track academic progress, cumulative CGPA, earned credits, and attendance.
6. **Faculty / Examiner Grading Portal**:
   - Real-time marks entry with instant total, letter grade, and grade point calculation.
   - Lock & submit marks directly to the Controller of Examinations.
7. **Controller of Examinations (CoE) Suite**:
   - Batch result publishing (Draft $\rightarrow$ Published).
   - 3-mark moderation grace policy for borderline students.
8. **Re-evaluation & Scrutiny Portal**:
   - Submit applications for paper re-evaluation, re-totalling, or answer script copies.
   - Live status tracking (Submitted, Under Scrutiny, Marks Revised).
9. **Analytics & Merit Rank Lists**:
   - University topper list with medals (🥇 Rank 1, 🥈 Rank 2, 🥉 Rank 3).
   - Department-wise pass percentage comparison bars.
10. **Examination Circulars & Code of Conduct**:
    - Examination rules, malpractice regulations, and downloadable guidelines.

---

## 🏗️ Architecture

```
c:\Users\srive\Documents\JAVA\SaranJavaProject\
├── src\main\java\com\examflow\
│   ├── Main.java                 # Entry point, boots DataStore & ExamServer
│   ├── model\                   # Student, Subject, Schedule, MarksRecord, Result, Reval, Notice
│   ├── repository\DataStore.java # In-memory database with JSON file persistence
│   ├── service\                 # GradeCalculator, ExamService, ResultService, RevaluationService
│   ├── server\                  # ExamServer (native Java 21 HttpServer) & StaticFileHandler
│   ├── util\SimpleJson.java     # Zero-dependency JSON serializer & deserializer
│   └── cli\ExamCli.java         # Interactive terminal CLI console
├── web\
│   ├── index.html               # Semantic HTML layout with 10 dedicated views
│   ├── css\styles.css           # Vanilla CSS with light/dark theme & print stylesheets
│   └── js\
│       ├── app.js               # Reactive router & UI event handlers
│       ├── data.js              # REST API bridge with seamless fallback
│       └── qrcode.js            # Self-contained SVG QR code & barcode generator
├── data\                        # Automatically synchronized JSON records
├── build.bat                    # Compiles all Java classes
├── run.bat                      # Compiles and starts the system on http://localhost:8085
└── README.md
```

---

## 🚀 How to Run

### Option 1: One-Click Windows Launcher
Double-click `run.bat` or run in terminal:
```cmd
run.bat
```

### Option 2: Manual Compilation & Execution
```cmd
# 1. Compile Java files
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src/main/java -Recurse -Filter *.java).FullName

# 2. Run the Java server
java -cp bin com.examflow.Main 8085
```
Open your browser at: `http://localhost:8085/`

### Option 3: Terminal CLI Console
For viva or command-line demonstrations:
```cmd
java -cp bin com.examflow.cli.ExamCli
```

---

## 🧪 Sample Test Data
- `2024CS101` — Saran V (B.Tech CSE, Sem 4, 9.60 SGPA, 9.42 CGPA, First Class with Distinction)
- `2024CS102` — Priya Sharma (B.Tech CSE, Sem 4, 8.85 SGPA, Distinction)
- `2024CS103` — Aditya Verma (B.Tech CSE, Sem 4, 7.60 SGPA, Re-evaluation candidate)
- `2024EC302` — Rahul Mehta (B.Tech ECE, Sem 4, 1 Arrear in Signals & Systems)
