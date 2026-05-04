package com.xtu.system.modules.dashboard.mapper;

import com.xtu.system.modules.dashboard.vo.DashboardDepartmentStatVO;
import com.xtu.system.modules.dashboard.vo.DashboardTodoItemVO;
import com.xtu.system.modules.dashboard.vo.DashboardTrendItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardMapper {

    long countUsers();

    long countStudents();

    long countTeachers();

    long countCourses();

    long countPendingApplications();

    List<DashboardTodoItemVO> selectTodoList(@Param("limit") int limit);

    List<DashboardDepartmentStatVO> selectDepartmentDistribution();

    List<DashboardTrendItemVO> selectRecentActivityTrend();
}
