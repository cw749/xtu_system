package com.xtu.system.modules.business.course.service.impl;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.modules.business.course.dto.CourseCreateRequest;
import com.xtu.system.modules.business.course.dto.CourseQueryRequest;
import com.xtu.system.modules.business.course.dto.CourseUpdateRequest;
import com.xtu.system.modules.business.course.entity.CourseEntity;
import com.xtu.system.modules.business.course.mapper.CourseMapper;
import com.xtu.system.modules.business.course.service.CourseService;
import com.xtu.system.modules.business.course.vo.CourseDetailVO;
import com.xtu.system.modules.business.course.vo.CourseOptionVO;
import com.xtu.system.modules.business.course.vo.CoursePageItemVO;
import com.xtu.system.modules.file.attachment.service.AttachmentService;
import com.xtu.system.modules.organization.department.mapper.DepartmentMapper;
import com.xtu.system.modules.personnel.teacher.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final long SYSTEM_OPERATOR_ID = 1L;

    private final CourseMapper courseMapper;
    private final DepartmentMapper departmentMapper;
    private final TeacherMapper teacherMapper;
    private final AttachmentService attachmentService;

    public CourseServiceImpl(
        CourseMapper courseMapper,
        DepartmentMapper departmentMapper,
        TeacherMapper teacherMapper,
        AttachmentService attachmentService
    ) {
        this.courseMapper = courseMapper;
        this.departmentMapper = departmentMapper;
        this.teacherMapper = teacherMapper;
        this.attachmentService = attachmentService;
    }

    @Override
    public PageResponse<CoursePageItemVO> getCoursePage(CourseQueryRequest request) {
        List<CoursePageItemVO> list = courseMapper.selectCoursePage(request);
        long total = courseMapper.countCoursePage(request);
        return PageResponse.of(list, request.getPageNum(), request.getPageSize(), total);
    }

    @Override
    public CourseDetailVO getCourseDetail(Long id) {
        CourseDetailVO detail = courseMapper.selectCourseDetailById(id);
        if (detail == null) {
            throw new BusinessException("课程不存在");
        }
        return detail;
    }

    @Override
    public List<CourseOptionVO> getCourseOptions() {
        return courseMapper.selectCourseOptions();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCourse(CourseCreateRequest request) {
        validateCourseCode(request.getCourseCode(), null);
        validateDepartment(request.getDeptId());
        validateTeacher(request.getTeacherId());

        CourseEntity entity = new CourseEntity();
        fillCourseEntity(entity, request.getCourseCode(), request.getCourseName(), request.getDeptId(), request.getTeacherId(),
            request.getCredit(), request.getCourseType(), request.getSemester(), request.getStatus(), request.getRemark());
        entity.setCreatedBy(SYSTEM_OPERATOR_ID);
        entity.setUpdatedBy(SYSTEM_OPERATOR_ID);
        courseMapper.insertCourse(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCourse(Long id, CourseUpdateRequest request) {
        CourseEntity existing = courseMapper.selectCourseEntityById(id);
        if (existing == null) {
            throw new BusinessException("课程不存在");
        }
        validateCourseCode(request.getCourseCode(), id);
        validateDepartment(request.getDeptId());
        validateTeacher(request.getTeacherId());
        fillCourseEntity(existing, request.getCourseCode(), request.getCourseName(), request.getDeptId(), request.getTeacherId(),
            request.getCredit(), request.getCourseType(), request.getSemester(), request.getStatus(), request.getRemark());
        existing.setUpdatedBy(SYSTEM_OPERATOR_ID);
        courseMapper.updateCourse(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(Long id) {
        if (courseMapper.logicDeleteCourse(id, SYSTEM_OPERATOR_ID) == 0) {
            throw new BusinessException("课程不存在");
        }
        attachmentService.deleteAttachmentsByBiz("course", id, SYSTEM_OPERATOR_ID);
    }

    private void validateCourseCode(String courseCode, Long currentId) {
        CourseEntity existing = courseMapper.selectCourseEntityByCode(courseCode);
        if (existing != null && !existing.getId().equals(currentId)) {
            throw new BusinessException("课程编码已存在");
        }
    }

    private void validateDepartment(Long deptId) {
        if (deptId == null || departmentMapper.selectDepartmentById(deptId) == null) {
            throw new BusinessException("所属部门不存在");
        }
    }

    private void validateTeacher(Long teacherId) {
        if (teacherId != null && teacherMapper.selectTeacherEntityById(teacherId) == null) {
            throw new BusinessException("授课教师不存在");
        }
    }

    private void fillCourseEntity(
        CourseEntity entity,
        String courseCode,
        String courseName,
        Long deptId,
        Long teacherId,
        BigDecimal credit,
        String courseType,
        String semester,
        Integer status,
        String remark
    ) {
        entity.setCourseCode(courseCode);
        entity.setCourseName(courseName);
        entity.setDeptId(deptId);
        entity.setTeacherId(teacherId);
        entity.setCredit(credit == null ? BigDecimal.ZERO : credit);
        entity.setCourseType(courseType);
        entity.setSemester(semester);
        entity.setStatus(status == null ? 1 : status);
        entity.setRemark(remark);
    }
}
