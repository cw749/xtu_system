package com.xtu.system.modules.personnel.student.service;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.personnel.student.dto.StudentAccountCreateRequest;
import com.xtu.system.modules.personnel.student.dto.StudentCreateRequest;
import com.xtu.system.modules.personnel.student.dto.StudentQueryRequest;
import com.xtu.system.modules.personnel.student.dto.StudentUpdateRequest;
import com.xtu.system.modules.personnel.student.vo.StudentDetailVO;
import com.xtu.system.modules.personnel.student.vo.StudentPageItemVO;

import java.util.List;

public interface StudentService {

    PageResponse<StudentPageItemVO> getStudentPage(StudentQueryRequest request);

    StudentDetailVO getStudentDetail(Long id);

    Long createStudent(StudentCreateRequest request);

    void updateStudent(Long id, StudentUpdateRequest request);

    void deleteStudent(Long id);

    void deleteStudents(List<Long> ids);

    Long createStudentAccount(Long id, StudentAccountCreateRequest request);

    void removeStudentAccount(Long id);

    int importStudents(List<StudentCreateRequest> requests);

    byte[] exportStudents(StudentQueryRequest request);
}
