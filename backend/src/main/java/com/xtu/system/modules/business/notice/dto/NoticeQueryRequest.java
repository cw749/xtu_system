package com.xtu.system.modules.business.notice.dto;

public class NoticeQueryRequest {

    private long pageNum = 1;
    private long pageSize = 10;
    private String keyword;
    private String noticeType;
    private Integer publishStatus;

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

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public long getOffset() {
        long normalizedPageNum = pageNum < 1 ? 1 : pageNum;
        long normalizedPageSize = pageSize < 1 ? 10 : pageSize;
        return (normalizedPageNum - 1) * normalizedPageSize;
    }
}
