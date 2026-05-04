package com.xtu.system.modules.system.role.service;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.system.role.dto.RoleCreateRequest;
import com.xtu.system.modules.system.role.dto.RoleQueryRequest;
import com.xtu.system.modules.system.role.dto.RoleUpdateRequest;
import com.xtu.system.modules.system.role.vo.RoleDetailVO;
import com.xtu.system.modules.system.role.vo.RoleOptionVO;
import com.xtu.system.modules.system.role.vo.RolePageItemVO;

import java.util.List;

public interface RoleService {

    PageResponse<RolePageItemVO> getRolePage(RoleQueryRequest request);

    RoleDetailVO getRoleDetail(Long id);

    Long createRole(RoleCreateRequest request);

    void updateRole(Long id, RoleUpdateRequest request);

    void deleteRole(Long id);

    void updateRoleStatus(Long id, Integer status);

    void assignRoleMenus(Long id, List<Long> menuIds);

    List<RoleOptionVO> getRoleOptions();
}
