package com.xtu.system.modules.system.role.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.system.role.dto.RoleCreateRequest;
import com.xtu.system.modules.system.role.dto.RoleMenuAssignRequest;
import com.xtu.system.modules.system.role.dto.RoleQueryRequest;
import com.xtu.system.modules.system.role.dto.RoleStatusUpdateRequest;
import com.xtu.system.modules.system.role.dto.RoleUpdateRequest;
import com.xtu.system.modules.system.role.service.RoleService;
import com.xtu.system.modules.system.role.vo.RoleDetailVO;
import com.xtu.system.modules.system.role.vo.RoleOptionVO;
import com.xtu.system.modules.system.role.vo.RolePageItemVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('system:role:view')")
    public ApiResponse<PageResponse<RolePageItemVO>> getRolePage(RoleQueryRequest request) {
        return ApiResponse.success(roleService.getRolePage(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:view')")
    public ApiResponse<RoleDetailVO> getRoleDetail(@PathVariable Long id) {
        return ApiResponse.success(roleService.getRoleDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:role:create')")
    public ApiResponse<Long> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return ApiResponse.success("创建成功", roleService.createRole(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:update')")
    public ApiResponse<Boolean> updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        roleService.updateRole(id, request);
        return ApiResponse.success("更新成功", Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public ApiResponse<Boolean> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success("删除成功", Boolean.TRUE);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('system:role:update')")
    public ApiResponse<Boolean> updateRoleStatus(@PathVariable Long id, @Valid @RequestBody RoleStatusUpdateRequest request) {
        roleService.updateRoleStatus(id, request.getStatus());
        return ApiResponse.success("状态更新成功", Boolean.TRUE);
    }

    @PutMapping("/{id}/menus")
    @PreAuthorize("hasAuthority('system:role:update')")
    public ApiResponse<Boolean> assignRoleMenus(@PathVariable Long id, @Valid @RequestBody RoleMenuAssignRequest request) {
        roleService.assignRoleMenus(id, request.getMenuIds());
        return ApiResponse.success("菜单分配成功", Boolean.TRUE);
    }

    @GetMapping("/options")
    @PreAuthorize("hasAuthority('system:role:view')")
    public ApiResponse<List<RoleOptionVO>> getRoleOptions() {
        return ApiResponse.success(roleService.getRoleOptions());
    }
}
