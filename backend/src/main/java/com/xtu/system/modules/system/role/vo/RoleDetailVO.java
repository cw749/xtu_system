package com.xtu.system.modules.system.role.vo;

import java.util.List;

public class RoleDetailVO extends RolePageItemVO {

    private List<Long> menuIds;

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
}
