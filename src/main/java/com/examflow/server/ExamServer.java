package com.examflow.server;

import com.examflow.model.*;
import com.examflow.repository.DataStore;
import com.examflow.service.*;
import com.examflow.util.SimpleJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;

public class ExamServer {
    private final int port;
    private HttpServer server;
    private final DataStore store = DataStore.getInstance();
    private final ExamService examService = new ExamService();
    private final ResultService resultService = new ResultService();
    private final RevaluationService revaluationService = new RevaluationService();

    public ExamServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        // REST API Contexts
        server.createContext("/api/status", this::handleStatus);
        server.createContext("/api/students", this::handleStudents);
        server.createContext("/api/subjects", this::handleSubjects);
        server.createContext("/api/results/search", this::handleResultSearch);
        server.createContext("/api/results/all", this::handleResultsAll);
        server.createContext("/api/results/publish", this::handleResultPublish);
        server.createContext("/api/results/grace", this::handleGraceMarks);
        server.createContext("/api/schedule", this::handleSchedule);
        server.createContext("/api/marks/submit", this::handleMarksSubmit);
        server.createContext("/api/toppers", this::handleToppers);
        server.createContext("/api/analytics", this::handleAnalytics);
        server.createContext("/api/revaluation", this::handleRevaluation);
        server.createContext("/api/revaluation/update", this::handleRevaluationUpdate);
        server.createContext("/api/notices", this::handleNotices);

        // Static Files Handler
        String webPath = findWebDirectory();
        server.createContext("/", new StaticFileHandler(webPath));

        server.start();
        System.out.println("==========================================================");
        System.out.println("  APEX EXAMFLOW - EXAM & RESULT MANAGEMENT SYSTEM");
        System.out.println("  Server is live on: http://localhost:" + port + "/");
        System.out.println("  Serving Web UI from: " + webPath);
        System.out.println("==========================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(1);
        }
    }

    private String findWebDirectory() {
        File f1 = new File("web");
        if (f1.exists() && f1.isDirectory()) return "web";
        File f2 = new File("src/main/resources/web");
        if (f2.exists() && f2.isDirectory()) return "src/main/resources/web";
        return ".";
    }

    // Handlers
    private void handleStatus(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", "ONLINE");
        map.put("system", "Apex Exam & Result Management System");
        map.put("version", "2.4.0-LTS");
        map.put("timestamp", System.currentTimeMillis());
        sendJsonResponse(exchange, 200, map);
    }

    private void handleStudents(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
        String rollNo = query.get("rollNo");
        if (rollNo != null && !rollNo.isEmpty()) {
            Student s = store.getStudent(rollNo.trim().toUpperCase());
            if (s != null) {
                sendJsonResponse(exchange, 200, s);
            } else {
                sendJsonResponse(exchange, 404, Map.of("error", "Student not found with Roll No: " + rollNo));
            }
        } else {
            sendJsonResponse(exchange, 200, new ArrayList<>(store.getAllStudents()));
        }
    }

    private void handleSubjects(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        sendJsonResponse(exchange, 200, new ArrayList<>(store.getAllSubjects()));
    }

    private void handleResultSearch(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
        String rollNo = query.get("rollNo");
        String semStr = query.get("semester");
        int semester = 4;
        if (semStr != null) {
            try { semester = Integer.parseInt(semStr); } catch (Exception ignored) {}
        }

        if (rollNo == null || rollNo.trim().isEmpty()) {
            sendJsonResponse(exchange, 400, Map.of("error", "Missing parameter: rollNo"));
            return;
        }

        StudentResult result = resultService.getResult(rollNo, semester);
        if (result != null) {
            sendJsonResponse(exchange, 200, result);
        } else {
            // Check if student exists at all
            Student s = store.getStudent(rollNo.trim().toUpperCase());
            if (s == null) {
                sendJsonResponse(exchange, 404, Map.of("error", "Invalid Roll Number: " + rollNo));
            } else {
                sendJsonResponse(exchange, 404, Map.of("error", "No published result found for " + rollNo + " in Semester " + semester));
            }
        }
    }

    private void handleResultsAll(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        sendJsonResponse(exchange, 200, new ArrayList<>(store.getAllResults()));
    }

    private void handleResultPublish(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, Map.of("error", "Method Not Allowed"));
            return;
        }
        String body = readBody(exchange);
        Map<String, Object> req = SimpleJson.parseObject(body);
        int semester = req.containsKey("semester") ? ((Number) req.get("semester")).intValue() : 4;
        boolean publish = req.containsKey("publish") ? Boolean.parseBoolean(String.valueOf(req.get("publish"))) : true;

        int affected = resultService.batchPublishResults(semester, publish);
        sendJsonResponse(exchange, 200, Map.of("success", true, "semester", semester, "publishedCount", affected));
    }

    private void handleGraceMarks(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, Map.of("error", "Method Not Allowed"));
            return;
        }
        String body = readBody(exchange);
        Map<String, Object> req = SimpleJson.parseObject(body);
        int semester = req.containsKey("semester") ? ((Number) req.get("semester")).intValue() : 4;
        double grace = req.containsKey("graceMarks") ? ((Number) req.get("graceMarks")).doubleValue() : 3.0;

        int affected = resultService.applyGraceMarks(semester, grace);
        sendJsonResponse(exchange, 200, Map.of("success", true, "semester", semester, "affectedStudents", affected));
    }

    private void handleSchedule(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = readBody(exchange);
            Map<String, Object> req = SimpleJson.parseObject(body);
            String code = (String) req.get("subjectCode");
            String name = (String) req.get("subjectName");
            String date = (String) req.get("examDate");
            String session = (String) req.get("session");
            String time = (String) req.get("timeSlot");
            String venue = (String) req.get("venueHall");
            int sem = req.containsKey("semester") ? ((Number) req.get("semester")).intValue() : 4;
            String dept = (String) req.get("department");
            String type = (String) req.get("examType");

            ExamScheduleItem item = examService.createSchedule(code, name, date, session, time, venue, sem, dept, type);
            sendJsonResponse(exchange, 201, item);
            return;
        }

        Map<String, String> q = parseQueryParams(exchange.getRequestURI().getQuery());
        String dept = q.get("department");
        String semStr = q.get("semester");
        Integer sem = semStr != null && !semStr.isEmpty() ? Integer.parseInt(semStr) : null;
        String type = q.get("examType");

        List<ExamScheduleItem> list = examService.getSchedules(dept, sem, type);
        sendJsonResponse(exchange, 200, list);
    }

    private void handleMarksSubmit(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, Map.of("error", "Method Not Allowed"));
            return;
        }
        String body = readBody(exchange);
        Map<String, Object> req = SimpleJson.parseObject(body);
        String rollNo = (String) req.get("rollNo");
        int sem = req.containsKey("semester") ? ((Number) req.get("semester")).intValue() : 4;
        String subCode = (String) req.get("subjectCode");
        double internal = req.containsKey("internalMarks") ? ((Number) req.get("internalMarks")).doubleValue() : 0.0;
        double external = req.containsKey("externalMarks") ? ((Number) req.get("externalMarks")).doubleValue() : 0.0;

        try {
            StudentResult res = resultService.submitFacultyMarks(rollNo, sem, subCode, internal, external);
            sendJsonResponse(exchange, 200, Map.of("success", true, "result", res));
        } catch (Exception e) {
            sendJsonResponse(exchange, 400, Map.of("error", e.getMessage()));
        }
    }

    private void handleToppers(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        Map<String, String> q = parseQueryParams(exchange.getRequestURI().getQuery());
        String semStr = q.get("semester");
        int sem = semStr != null ? Integer.parseInt(semStr) : 4;
        sendJsonResponse(exchange, 200, resultService.getToppers(sem));
    }

    private void handleAnalytics(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        sendJsonResponse(exchange, 200, resultService.getAnalytics());
    }

    private void handleRevaluation(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = readBody(exchange);
            Map<String, Object> req = SimpleJson.parseObject(body);
            String rollNo = (String) req.get("rollNo");
            String code = (String) req.get("subjectCode");
            int sem = req.containsKey("semester") ? ((Number) req.get("semester")).intValue() : 4;
            String type = (String) req.get("serviceType");

            RevaluationRequest r = revaluationService.submitRequest(rollNo, code, sem, type);
            sendJsonResponse(exchange, 201, r);
            return;
        }

        sendJsonResponse(exchange, 200, revaluationService.getAll());
    }

    private void handleRevaluationUpdate(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, Map.of("error", "Method Not Allowed"));
            return;
        }
        String body = readBody(exchange);
        Map<String, Object> req = SimpleJson.parseObject(body);
        String id = (String) req.get("id");
        String status = (String) req.get("status");
        double revised = req.containsKey("revisedMarks") ? ((Number) req.get("revisedMarks")).doubleValue() : 0.0;
        String remarks = (String) req.get("remarks");

        RevaluationRequest r = revaluationService.updateStatus(id, status, revised, remarks);
        if (r != null) {
            sendJsonResponse(exchange, 200, r);
        } else {
            sendJsonResponse(exchange, 404, Map.of("error", "Request not found with id: " + id));
        }
    }

    private void handleNotices(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) return;
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = readBody(exchange);
            Map<String, Object> req = SimpleJson.parseObject(body);
            String title = (String) req.get("title");
            String cat = (String) req.get("category");
            String content = (String) req.get("content");
            boolean urgent = req.containsKey("isUrgent") && Boolean.parseBoolean(String.valueOf(req.get("isUrgent")));
            String attach = (String) req.get("attachmentName");

            Notice n = new Notice("NOT-" + System.currentTimeMillis() % 10000, title, cat, java.time.LocalDate.now().toString(), content, urgent, attach);
            store.addNotice(n);
            sendJsonResponse(exchange, 201, n);
            return;
        }

        sendJsonResponse(exchange, 200, store.getAllNotices());
    }

    // Helper methods
    private boolean handleCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, Object data) throws IOException {
        String json = SimpleJson.toJson(data);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String val = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                map.put(key, val);
            }
        }
        return map;
    }
}
