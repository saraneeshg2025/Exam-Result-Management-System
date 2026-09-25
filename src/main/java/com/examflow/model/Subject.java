package com.examflow.model;

public class Subject {
    private String code;
    private String name;
    private int credits;
    private String department;
    private int semester;
    private int maxInternal;
    private int maxExternal;
    private boolean isPractical;

    public Subject() {}

    public Subject(String code, String name, int credits, String department, int semester, int maxInternal, int maxExternal, boolean isPractical) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.department = department;
        this.semester = semester;
        this.maxInternal = maxInternal;
        this.maxExternal = maxExternal;
        this.isPractical = isPractical;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getMaxInternal() { return maxInternal; }
    public void setMaxInternal(int maxInternal) { this.maxInternal = maxInternal; }

    public int getMaxExternal() { return maxExternal; }
    public void setMaxExternal(int maxExternal) { this.maxExternal = maxExternal; }

    public boolean isPractical() { return isPractical; }
    public void setPractical(boolean practical) { isPractical = practical; }
}
