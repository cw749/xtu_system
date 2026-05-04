package com.xtu.system.modules.dashboard.service.impl;

import com.xtu.system.modules.dashboard.mapper.DashboardMapper;
import com.xtu.system.modules.dashboard.service.DashboardService;
import com.xtu.system.modules.dashboard.vo.DashboardSummaryVO;
import com.xtu.system.modules.dashboard.vo.DashboardTodoItemVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final int TODO_LIMIT = 8;

    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardMapper dashboardMapper) {
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    public DashboardSummaryVO getSummary() {
        DashboardSummaryVO summary = new DashboardSummaryVO();
        summary.setUserCount(dashboardMapper.countUsers());
        summary.setStudentCount(dashboardMapper.countStudents());
        summary.setTeacherCount(dashboardMapper.countTeachers());
        summary.setCourseCount(dashboardMapper.countCourses());
        summary.setPendingApplicationCount(dashboardMapper.countPendingApplications());
        return summary;
    }

    @Override
    public List<DashboardTodoItemVO> getTodoList() {
        return dashboardMapper.selectTodoList(TODO_LIMIT);
    }

    @Override
    public Map<String, Object> getCharts() {
        return Map.of(
            "departmentDistribution", dashboardMapper.selectDepartmentDistribution(),
            "applicationTrend", dashboardMapper.selectRecentActivityTrend()
        );
    }
}
