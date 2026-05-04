package com.xtu.system.modules.business.notice.dto;

import jakarta.validation.constraints.NotBlank;

public class NoticeCreateRequest {

    @NotBlank(message = "公告标题不能为空")
    private String title;

    private String noticeType;
    private String content;
    private Integer publishStatus;
    private Integer pinned;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Integer getPinned() {
        return pinned;
    }

    public void setPinned(Integer pinned) {
        this.pinned = pinned;
    }
}
