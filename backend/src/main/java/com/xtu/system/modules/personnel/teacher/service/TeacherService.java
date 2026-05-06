package com.xtu.system.modules.personnel.teacher.service;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.personnel.teacher.dto.TeacherAccountCreateRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherCreateRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherQueryRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherUpdateRequest;
import com.xtu.system.modules.personnel.teacher.vo.TeacherDetailVO;
import com.xtu.system.modules.personnel.teacher.vo.TeacherPageItemVO;

import java.util.List;

public interface TeacherService {

    PageResponse<TeacherPageItemVO> getTeacherPage(TeacherQueryRequest request);

    TeacherDetailVO getTeacherDetail(Long id);

    Long createTeacher(TeacherCreateRequest request);

    void updateTeacher(Long id, TeacherUpdateRequest request);

    void deleteTeacher(Long id);

    void deleteTeachers(List<Long> ids);

    Long createTeacherAccount(Long id, TeacherAccountCreateRequest request);

    void removeTeacherAccount(Long id);

    int importTeachers(List<TeacherCreateRequest> requests);

    byte[] exportTeachers(TeacherQueryRequest request);
}
