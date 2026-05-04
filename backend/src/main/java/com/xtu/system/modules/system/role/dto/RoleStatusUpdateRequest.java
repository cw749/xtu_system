package com.xtu.system.modules.system.role.dto;

import jakarta.validation.constraints.NotNull;

public class RoleStatusUpdateRequest {

    @NotNull(message = "状态不能为空")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
