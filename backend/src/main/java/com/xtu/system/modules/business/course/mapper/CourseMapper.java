package com.xtu.system.modules.business.course.mapper;

import com.xtu.system.modules.business.course.dto.CourseQueryRequest;
import com.xtu.system.modules.business.course.entity.CourseEntity;
import com.xtu.system.modules.business.course.vo.CourseDetailVO;
import com.xtu.system.modules.business.course.vo.CourseOptionVO;
import com.xtu.system.modules.business.course.vo.CoursePageItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {

    List<CoursePageItemVO> selectCoursePage(CourseQueryRequest request);

    long countCoursePage(CourseQueryRequest request);

    CourseDetailVO selectCourseDetailById(@Param("id") Long id);

    CourseEntity selectCourseEntityById(@Param("id") Long id);

    CourseEntity selectCourseEntityByCode(@Param("courseCode") String courseCode);

    List<CourseOptionVO> selectCourseOptions();

    int insertCourse(CourseEntity entity);

    int updateCourse(CourseEntity entity);

    int logicDeleteCourse(@Param("id") Long id, @Param("updatedBy") Long updatedBy);
}
