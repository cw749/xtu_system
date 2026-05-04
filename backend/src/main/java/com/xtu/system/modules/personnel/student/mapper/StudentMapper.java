package com.xtu.system.modules.personnel.student.mapper;

import com.xtu.system.modules.personnel.student.dto.StudentQueryRequest;
import com.xtu.system.modules.personnel.student.entity.StudentEntity;
import com.xtu.system.modules.personnel.student.vo.StudentDetailVO;
import com.xtu.system.modules.personnel.student.vo.StudentPageItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<StudentPageItemVO> selectStudentPage(StudentQueryRequest request);

    List<StudentPageItemVO> selectStudentExportList(StudentQueryRequest request);

    long countStudentPage(StudentQueryRequest request);

    StudentDetailVO selectStudentDetailById(@Param("id") Long id);

    StudentEntity selectStudentEntityById(@Param("id") Long id);

    StudentEntity selectStudentEntityByNo(@Param("studentNo") String studentNo);

    StudentEntity selectStudentEntityByUserId(@Param("userId") Long userId);

    int insertStudent(StudentEntity entity);

    int updateStudent(StudentEntity entity);

    int logicDeleteStudent(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    int clearUserBindingByUserId(@Param("userId") Long userId, @Param("updatedBy") Long updatedBy);
}
