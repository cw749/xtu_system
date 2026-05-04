package com.xtu.system.modules.file.attachment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AttachmentQueryRequest {

    @NotBlank(message = "业务类型不能为空")
    private String bizType;

    @NotNull(message = "业务ID不能为空")
    private Long bizId;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }
}
