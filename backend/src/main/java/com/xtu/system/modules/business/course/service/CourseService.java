package com.xtu.system.modules.business.course.service;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.business.course.dto.CourseCreateRequest;
import com.xtu.system.modules.business.course.dto.CourseQueryRequest;
import com.xtu.system.modules.business.course.dto.CourseUpdateRequest;
import com.xtu.system.modules.business.course.vo.CourseDetailVO;
import com.xtu.system.modules.business.course.vo.CourseOptionVO;
import com.xtu.system.modules.business.course.vo.CoursePageItemVO;

import java.util.List;

public interface CourseService {

    PageResponse<CoursePageItemVO> getCoursePage(CourseQueryRequest request);

    CourseDetailVO getCourseDetail(Long id);

    Long createCourse(CourseCreateRequest request);

    void updateCourse(Long id, CourseUpdateRequest request);

    List<CourseOptionVO> getCourseOptions();

    void deleteCourse(Long id);
}
