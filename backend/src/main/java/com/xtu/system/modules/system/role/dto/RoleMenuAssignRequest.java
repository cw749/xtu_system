package com.xtu.system.modules.system.role.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RoleMenuAssignRequest {

    @NotNull(message = "菜单集合不能为空")
    private List<Long> menuIds;

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
}
