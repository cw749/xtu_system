package com.xtu.system.modules.personnel.teacher.mapper;

import com.xtu.system.modules.personnel.teacher.dto.TeacherQueryRequest;
import com.xtu.system.modules.personnel.teacher.entity.TeacherEntity;
import com.xtu.system.modules.personnel.teacher.vo.TeacherDetailVO;
import com.xtu.system.modules.personnel.teacher.vo.TeacherPageItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherMapper {

    List<TeacherPageItemVO> selectTeacherPage(TeacherQueryRequest request);

    List<TeacherPageItemVO> selectTeacherExportList(TeacherQueryRequest request);

    long countTeacherPage(TeacherQueryRequest request);

    TeacherDetailVO selectTeacherDetailById(@Param("id") Long id);

    TeacherEntity selectTeacherEntityById(@Param("id") Long id);

    TeacherEntity selectTeacherEntityByNo(@Param("teacherNo") String teacherNo);

    TeacherEntity selectTeacherEntityByUserId(@Param("userId") Long userId);

    int insertTeacher(TeacherEntity entity);

    int updateTeacher(TeacherEntity entity);

    int logicDeleteTeacher(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    int clearUserBindingByUserId(@Param("userId") Long userId, @Param("updatedBy") Long updatedBy);
}
