package com.examflow;

import com.examflow.repository.DataStore;
import com.examflow.server.ExamServer;

public class Main {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  Starting Exam & Result Management System (Java 21)");
        System.out.println("===============================================================");

        // Warm up DataStore
        DataStore store = DataStore.getInstance();
        System.out.println("Loaded " + store.getAllStudents().size() + " students.");
        System.out.println("Loaded " + store.getAllSubjects().size() + " subjects.");
        System.out.println("Loaded " + store.getAllSchedules().size() + " exam schedules.");
        System.out.println("Loaded " + store.getAllResults().size() + " semester results.");
        System.out.println("Loaded " + store.getAllNotices().size() + " circular notices.");

        int port = 8085;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception ignored) {}
        }

        ExamServer server = new ExamServer(port);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Could not start on port " + port + ": " + e.getMessage());
            int fallbackPort = 8090;
            System.out.println("Attempting fallback port " + fallbackPort + "...");
            try {
                server = new ExamServer(fallbackPort);
                server.start();
            } catch (Exception ex) {
                System.err.println("Fatal: Failed to start server: " + ex.getMessage());
                System.exit(1);
            }
        }

        // Keep process alive
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Exam & Result Management System...");
        }));
    }
}
