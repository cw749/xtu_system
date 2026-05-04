package com.xtu.system.modules.personnel.teacher.dto;

public class TeacherQueryRequest {

    private long pageNum = 1;
    private long pageSize = 10;
    private String keyword;
    private Long deptId;
    private Integer status;

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getOffset() {
        long normalizedPageNum = pageNum < 1 ? 1 : pageNum;
        long normalizedPageSize = pageSize < 1 ? 10 : pageSize;
        return (normalizedPageNum - 1) * normalizedPageSize;
    }
}
