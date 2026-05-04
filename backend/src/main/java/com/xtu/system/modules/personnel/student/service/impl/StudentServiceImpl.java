package com.xtu.system.modules.personnel.student.service.impl;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.common.util.CsvExportUtils;
import com.xtu.system.modules.organization.department.mapper.DepartmentMapper;
import com.xtu.system.modules.personnel.student.dto.StudentAccountCreateRequest;
import com.xtu.system.modules.personnel.student.dto.StudentCreateRequest;
import com.xtu.system.modules.personnel.student.dto.StudentQueryRequest;
import com.xtu.system.modules.personnel.student.dto.StudentUpdateRequest;
import com.xtu.system.modules.personnel.student.entity.StudentEntity;
import com.xtu.system.modules.personnel.student.mapper.StudentMapper;
import com.xtu.system.modules.personnel.student.service.StudentService;
import com.xtu.system.modules.personnel.student.vo.StudentDetailVO;
import com.xtu.system.modules.personnel.student.vo.StudentPageItemVO;
import com.xtu.system.modules.system.role.entity.RoleEntity;
import com.xtu.system.modules.system.role.mapper.RoleMapper;
import com.xtu.system.modules.system.user.entity.UserEntity;
import com.xtu.system.modules.system.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final long SYSTEM_OPERATOR_ID = 1L;
    private static final int STUDENT_USER_TYPE = 3;
    private static final String STUDENT_ROLE_CODE = "student";

    private final DepartmentMapper departmentMapper;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(
        DepartmentMapper departmentMapper,
        StudentMapper studentMapper,
        UserMapper userMapper,
        RoleMapper roleMapper,
        PasswordEncoder passwordEncoder
    ) {
        this.departmentMapper = departmentMapper;
        this.studentMapper = studentMapper;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageResponse<StudentPageItemVO> getStudentPage(StudentQueryRequest request) {
        List<StudentPageItemVO> list = studentMapper.selectStudentPage(request);
        long total = studentMapper.countStudentPage(request);
        return PageResponse.of(list, request.getPageNum(), request.getPageSize(), total);
    }

    @Override
    public StudentDetailVO getStudentDetail(Long id) {
        StudentDetailVO detail = studentMapper.selectStudentDetailById(id);
        if (detail == null) {
            throw new BusinessException("学生不存在");
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createStudent(StudentCreateRequest request) {
        validateStudentNo(request.getStudentNo(), null);
        validateDepartment(request.getDeptId());
        validateUserBinding(request.getUserId(), null);
        StudentEntity entity = new StudentEntity();
        fillStudentEntity(entity, request.getUserId(), request.getStudentNo(), request.getStudentName(), request.getGender(),
            request.getDeptId(), request.getMajorName(), request.getGradeYear(), request.getClassName(),
            request.getPhone(), request.getEmail(), request.getStatus(), request.getRemark());
        entity.setCreatedBy(SYSTEM_OPERATOR_ID);
        entity.setUpdatedBy(SYSTEM_OPERATOR_ID);
        studentMapper.insertStudent(entity);
        syncLinkedUser(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStudent(Long id, StudentUpdateRequest request) {
        StudentEntity existing = studentMapper.selectStudentEntityById(id);
        if (existing == null) {
            throw new BusinessException("学生不存在");
        }
        validateStudentNo(request.getStudentNo(), id);
        validateDepartment(request.getDeptId());
        validateUserBinding(request.getUserId(), id);
        fillStudentEntity(existing, request.getUserId(), request.getStudentNo(), request.getStudentName(), request.getGender(),
            request.getDeptId(), request.getMajorName(), request.getGradeYear(), request.getClassName(),
            request.getPhone(), request.getEmail(), request.getStatus(), request.getRemark());
        existing.setUpdatedBy(SYSTEM_OPERATOR_ID);
        studentMapper.updateStudent(existing);
        syncLinkedUser(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Long id) {
        StudentEntity existing = studentMapper.selectStudentEntityById(id);
        if (existing == null) {
            throw new BusinessException("学生不存在");
        }
        removeLinkedUser(existing.getUserId());
        if (studentMapper.logicDeleteStudent(id, SYSTEM_OPERATOR_ID) == 0) {
            throw new BusinessException("学生不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudents(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("待删除学生不能为空");
        }
        for (Long id : new LinkedHashSet<>(ids)) {
            deleteStudent(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createStudentAccount(Long id, StudentAccountCreateRequest request) {
        StudentEntity student = studentMapper.selectStudentEntityById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        if (student.getUserId() != null) {
            throw new BusinessException("当前学生已绑定系统账号");
        }
        if (userMapper.selectUserEntityByUsername(request.getUsername()) != null) {
            throw new BusinessException("账号已存在");
        }

        RoleEntity role = roleMapper.selectRoleEntityByCode(STUDENT_ROLE_CODE);
        if (role == null) {
            throw new BusinessException("学生角色未初始化");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRealName(student.getStudentName());
        user.setUserType(STUDENT_USER_TYPE);
        user.setDeptId(student.getDeptId());
        user.setGender(student.getGender());
        user.setPhone(student.getPhone());
        user.setEmail(student.getEmail());
        user.setStatus(request.getStatus() == null ? student.getStatus() : request.getStatus());
        user.setCreatedBy(SYSTEM_OPERATOR_ID);
        user.setUpdatedBy(SYSTEM_OPERATOR_ID);
        userMapper.insertUser(user);
        userMapper.insertUserRoles(user.getId(), SYSTEM_OPERATOR_ID, List.of(role.getId()));

        student.setUserId(user.getId());
        student.setUpdatedBy(SYSTEM_OPERATOR_ID);
        studentMapper.updateStudent(student);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeStudentAccount(Long id) {
        StudentEntity student = studentMapper.selectStudentEntityById(id);
        if (student == null) {
            throw new BusinessException("学生不存在");
        }
        if (student.getUserId() == null) {
            throw new BusinessException("当前学生未绑定系统账号");
        }
        removeLinkedUser(student.getUserId());
        student.setUserId(null);
        student.setUpdatedBy(SYSTEM_OPERATOR_ID);
        studentMapper.updateStudent(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importStudents(List<StudentCreateRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BusinessException("导入数据不能为空");
        }
        int count = 0;
        for (StudentCreateRequest request : requests) {
            createStudent(request);
            count++;
        }
        return count;
    }

    @Override
    public byte[] exportStudents(StudentQueryRequest request) {
        List<StudentPageItemVO> list = studentMapper.selectStudentExportList(request);
        List<List<String>> rows = new ArrayList<>();
        for (StudentPageItemVO item : list) {
            rows.add(List.of(
                valueOf(item.getStudentNo()),
                valueOf(item.getStudentName()),
                valueOf(item.getDeptName()),
                valueOf(item.getMajorName()),
                valueOf(item.getGradeYear()),
                valueOf(item.getClassName()),
                valueOf(item.getPhone()),
                valueOf(item.getEmail()),
                item.getStatus() != null && item.getStatus() == 1 ? "在读" : "离校",
                valueOf(item.getAccountUsername())
            ));
        }
        return CsvExportUtils.buildCsv(
            List.of("学号", "姓名", "部门", "专业", "年级", "班级", "手机号", "邮箱", "状态", "系统账号"),
            rows
        );
    }

    private void validateStudentNo(String studentNo, Long currentId) {
        StudentEntity existing = studentMapper.selectStudentEntityByNo(studentNo);
        if (existing != null && !existing.getId().equals(currentId)) {
            throw new BusinessException("学号已存在");
        }
    }

    private void validateUserBinding(Long userId, Long currentId) {
        if (userId == null) {
            return;
        }
        UserEntity user = userMapper.selectUserEntityById(userId);
        if (user == null) {
            throw new BusinessException("绑定的用户不存在");
        }
        if (user.getUserType() != STUDENT_USER_TYPE) {
            throw new BusinessException("绑定用户类型必须为学生");
        }
        StudentEntity existing = studentMapper.selectStudentEntityByUserId(userId);
        if (existing != null && !existing.getId().equals(currentId)) {
            throw new BusinessException("该账号已绑定其他学生档案");
        }
    }

    private void validateDepartment(Long deptId) {
        if (deptId == null || departmentMapper.selectDepartmentById(deptId) == null) {
            throw new BusinessException("所属部门不存在");
        }
    }

    private void fillStudentEntity(
        StudentEntity entity,
        Long userId,
        String studentNo,
        String studentName,
        Integer gender,
        Long deptId,
        String majorName,
        Integer gradeYear,
        String className,
        String phone,
        String email,
        Integer status,
        String remark
    ) {
        entity.setUserId(userId);
        entity.setStudentNo(studentNo);
        entity.setStudentName(studentName);
        entity.setGender(gender);
        entity.setDeptId(deptId);
        entity.setMajorName(majorName);
        entity.setGradeYear(gradeYear);
        entity.setClassName(className);
        entity.setPhone(phone);
        entity.setEmail(email);
        entity.setStatus(status == null ? 1 : status);
        entity.setRemark(remark);
    }

    private void syncLinkedUser(StudentEntity student) {
        if (student.getUserId() == null) {
            return;
        }
        UserEntity user = userMapper.selectUserEntityById(student.getUserId());
        if (user == null) {
            throw new BusinessException("绑定的用户不存在");
        }
        user.setRealName(student.getStudentName());
        user.setUserType(STUDENT_USER_TYPE);
        user.setDeptId(student.getDeptId());
        user.setGender(student.getGender());
        user.setPhone(student.getPhone());
        user.setEmail(student.getEmail());
        user.setStatus(student.getStatus());
        user.setUpdatedBy(SYSTEM_OPERATOR_ID);
        userMapper.updateUser(user);
    }

    private void removeLinkedUser(Long userId) {
        if (userId == null) {
            return;
        }
        UserEntity user = userMapper.selectUserEntityById(userId);
        if (user != null) {
            userMapper.deleteUserRolesByUserId(userId);
            userMapper.logicDeleteUser(userId, SYSTEM_OPERATOR_ID);
        }
    }

    private String valueOf(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
