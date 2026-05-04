package com.xtu.system.modules.business.course.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.business.course.dto.CourseCreateRequest;
import com.xtu.system.modules.business.course.dto.CourseQueryRequest;
import com.xtu.system.modules.business.course.dto.CourseUpdateRequest;
import com.xtu.system.modules.business.course.service.CourseService;
import com.xtu.system.modules.business.course.vo.CourseDetailVO;
import com.xtu.system.modules.business.course.vo.CourseOptionVO;
import com.xtu.system.modules.business.course.vo.CoursePageItemVO;
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
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('business:course:view')")
    public ApiResponse<PageResponse<CoursePageItemVO>> getCoursePage(CourseQueryRequest request) {
        return ApiResponse.success(courseService.getCoursePage(request));
    }

    @GetMapping("/options")
    @PreAuthorize("hasAuthority('business:course:view')")
    public ApiResponse<List<CourseOptionVO>> getCourseOptions() {
        return ApiResponse.success(courseService.getCourseOptions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:course:view')")
    public ApiResponse<CourseDetailVO> getCourseDetail(@PathVariable Long id) {
        return ApiResponse.success(courseService.getCourseDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('business:course:create')")
    public ApiResponse<Long> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        return ApiResponse.success("创建成功", courseService.createCourse(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('business:course:update')")
    public ApiResponse<Boolean> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseUpdateRequest request) {
        courseService.updateCourse(id, request);
        return ApiResponse.success("更新成功", Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:course:delete')")
    public ApiResponse<Boolean> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ApiResponse.success("删除成功", Boolean.TRUE);
    }
}
