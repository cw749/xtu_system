package com.xtu.system.modules.system.menu.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.modules.system.menu.dto.MenuCreateRequest;
import com.xtu.system.modules.system.menu.dto.MenuUpdateRequest;
import com.xtu.system.modules.system.menu.service.MenuService;
import com.xtu.system.modules.system.menu.vo.MenuTreeVO;
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
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:view')")
    public ApiResponse<List<MenuTreeVO>> getMenuTree() {
        return ApiResponse.success(menuService.getMenuTree());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:view')")
    public ApiResponse<MenuTreeVO> getMenuDetail(@PathVariable Long id) {
        return ApiResponse.success(menuService.getMenuDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:create')")
    public ApiResponse<Long> createMenu(@Valid @RequestBody MenuCreateRequest request) {
        return ApiResponse.success("创建成功", menuService.createMenu(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:update')")
    public ApiResponse<Boolean> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuUpdateRequest request) {
        menuService.updateMenu(id, request);
        return ApiResponse.success("更新成功", Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public ApiResponse<Boolean> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ApiResponse.success("删除成功", Boolean.TRUE);
    }
}
