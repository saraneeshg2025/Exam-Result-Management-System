package com.examflow.model;

public class Notice {
    private String id;
    private String title;
    private String category; // Examination, Timetable, Result, Revaluation, Circular
    private String date;
    private String content;
    private boolean isUrgent;
    private String attachmentName;

    public Notice() {}

    public Notice(String id, String title, String category, String date, String content, boolean isUrgent, String attachmentName) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.date = date;
        this.content = content;
        this.isUrgent = isUrgent;
        this.attachmentName = attachmentName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isUrgent() { return isUrgent; }
    public void setUrgent(boolean urgent) { isUrgent = urgent; }

    public String getAttachmentName() { return attachmentName; }
    public void setAttachmentName(String attachmentName) { this.attachmentName = attachmentName; }
}
