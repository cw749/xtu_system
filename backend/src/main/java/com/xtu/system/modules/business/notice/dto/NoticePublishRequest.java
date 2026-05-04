package com.xtu.system.modules.business.notice.dto;

import jakarta.validation.constraints.NotNull;

public class NoticePublishRequest {

    @NotNull(message = "发布状态不能为空")
    private Integer publishStatus;

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }
}
