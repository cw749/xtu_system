package com.xtu.system.modules.dashboard.vo;

public class DashboardSummaryVO {

    private Long userCount;
    private Long studentCount;
    private Long teacherCount;
    private Long courseCount;
    private Long pendingApplicationCount;

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Long studentCount) {
        this.studentCount = studentCount;
    }

    public Long getTeacherCount() {
        return teacherCount;
    }

    public void setTeacherCount(Long teacherCount) {
        this.teacherCount = teacherCount;
    }

    public Long getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(Long courseCount) {
        this.courseCount = courseCount;
    }

    public Long getPendingApplicationCount() {
        return pendingApplicationCount;
    }

    public void setPendingApplicationCount(Long pendingApplicationCount) {
        this.pendingApplicationCount = pendingApplicationCount;
    }
}
