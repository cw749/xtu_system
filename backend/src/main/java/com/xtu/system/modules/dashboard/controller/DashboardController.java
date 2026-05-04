package com.xtu.system.modules.dashboard.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.modules.dashboard.service.DashboardService;
import com.xtu.system.modules.dashboard.vo.DashboardSummaryVO;
import com.xtu.system.modules.dashboard.vo.DashboardTodoItemVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('dashboard:view')")
    public ApiResponse<DashboardSummaryVO> getSummary() {
        return ApiResponse.success(dashboardService.getSummary());
    }

    @GetMapping("/todo")
    @PreAuthorize("hasAuthority('dashboard:view')")
    public ApiResponse<List<DashboardTodoItemVO>> getTodoList() {
        return ApiResponse.success(dashboardService.getTodoList());
    }

    @GetMapping("/charts")
    @PreAuthorize("hasAuthority('dashboard:view')")
    public ApiResponse<Map<String, Object>> getCharts() {
        return ApiResponse.success(dashboardService.getCharts());
    }
}
