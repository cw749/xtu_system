package com.xtu.system.modules.dashboard.service;

import com.xtu.system.modules.dashboard.vo.DashboardSummaryVO;
import com.xtu.system.modules.dashboard.vo.DashboardTodoItemVO;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    DashboardSummaryVO getSummary();

    List<DashboardTodoItemVO> getTodoList();

    Map<String, Object> getCharts();
}
