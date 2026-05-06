package com.xtu.system.modules.personnel.teacher.service.impl;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.common.util.CsvExportUtils;
import com.xtu.system.modules.organization.department.mapper.DepartmentMapper;
import com.xtu.system.modules.personnel.teacher.dto.TeacherAccountCreateRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherCreateRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherQueryRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherUpdateRequest;
import com.xtu.system.modules.personnel.teacher.entity.TeacherEntity;
import com.xtu.system.modules.personnel.teacher.mapper.TeacherMapper;
import com.xtu.system.modules.personnel.teacher.service.TeacherService;
import com.xtu.system.modules.personnel.teacher.vo.TeacherDetailVO;
import com.xtu.system.modules.personnel.teacher.vo.TeacherPageItemVO;
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
public class TeacherServiceImpl implements TeacherService {

    private static final long SYSTEM_OPERATOR_ID = 1L;
    private static final int TEACHER_USER_TYPE = 2;
    private static final String TEACHER_ROLE_CODE = "teacher";

    private final DepartmentMapper departmentMapper;
    private final TeacherMapper teacherMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public TeacherServiceImpl(
        DepartmentMapper departmentMapper,
        TeacherMapper teacherMapper,
        UserMapper userMapper,
        RoleMapper roleMapper,
        PasswordEncoder passwordEncoder
    ) {
        this.departmentMapper = departmentMapper;
        this.teacherMapper = teacherMapper;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageResponse<TeacherPageItemVO> getTeacherPage(TeacherQueryRequest request) {
        List<TeacherPageItemVO> list = teacherMapper.selectTeacherPage(request);
        long total = teacherMapper.countTeacherPage(request);
        return PageResponse.of(list, request.getPageNum(), request.getPageSize(), total);
    }

    @Override
    public TeacherDetailVO getTeacherDetail(Long id) {
        TeacherDetailVO detail = teacherMapper.selectTeacherDetailById(id);
        if (detail == null) {
            throw new BusinessException("教师不存在");
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTeacher(TeacherCreateRequest request) {
        validateTeacherNo(request.getTeacherNo(), null);
        validateDepartment(request.getDeptId());
        validateUserBinding(request.getUserId(), null);
        TeacherEntity entity = new TeacherEntity();
        fillTeacherEntity(entity, request.getUserId(), request.getTeacherNo(), request.getTeacherName(), request.getGender(),
            request.getDeptId(), request.getTitleName(), request.getPhone(), request.getEmail(), request.getStatus(), request.getRemark());
        entity.setCreatedBy(SYSTEM_OPERATOR_ID);
        entity.setUpdatedBy(SYSTEM_OPERATOR_ID);
        teacherMapper.insertTeacher(entity);
        syncLinkedUser(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeacher(Long id, TeacherUpdateRequest request) {
        TeacherEntity existing = teacherMapper.selectTeacherEntityById(id);
        if (existing == null) {
            throw new BusinessException("教师不存在");
        }
        validateTeacherNo(request.getTeacherNo(), id);
        validateDepartment(request.getDeptId());
        validateUserBinding(request.getUserId(), id);
        fillTeacherEntity(existing, request.getUserId(), request.getTeacherNo(), request.getTeacherName(), request.getGender(),
            request.getDeptId(), request.getTitleName(), request.getPhone(), request.getEmail(), request.getStatus(), request.getRemark());
        existing.setUpdatedBy(SYSTEM_OPERATOR_ID);
        teacherMapper.updateTeacher(existing);
        syncLinkedUser(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeacher(Long id) {
        TeacherEntity existing = teacherMapper.selectTeacherEntityById(id);
        if (existing == null) {
            throw new BusinessException("教师不存在");
        }
        removeLinkedUser(existing.getUserId());
        if (teacherMapper.logicDeleteTeacher(id, SYSTEM_OPERATOR_ID) == 0) {
            throw new BusinessException("教师不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeachers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("待删除教师不能为空");
        }
        for (Long id : new LinkedHashSet<>(ids)) {
            deleteTeacher(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTeacherAccount(Long id, TeacherAccountCreateRequest request) {
        TeacherEntity teacher = teacherMapper.selectTeacherEntityById(id);
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }
        if (teacher.getUserId() != null) {
            throw new BusinessException("当前教师已绑定系统账号");
        }
        if (userMapper.selectUserEntityByUsername(request.getUsername()) != null) {
            throw new BusinessException("账号已存在");
        }

        RoleEntity role = roleMapper.selectRoleEntityByCode(TEACHER_ROLE_CODE);
        if (role == null) {
            throw new BusinessException("教师角色未初始化");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRealName(teacher.getTeacherName());
        user.setUserType(TEACHER_USER_TYPE);
        user.setDeptId(teacher.getDeptId());
        user.setGender(teacher.getGender());
        user.setPhone(teacher.getPhone());
        user.setEmail(teacher.getEmail());
        user.setStatus(request.getStatus() == null ? teacher.getStatus() : request.getStatus());
        user.setCreatedBy(SYSTEM_OPERATOR_ID);
        user.setUpdatedBy(SYSTEM_OPERATOR_ID);
        userMapper.insertUser(user);
        userMapper.insertUserRoles(user.getId(), SYSTEM_OPERATOR_ID, List.of(role.getId()));

        teacher.setUserId(user.getId());
        teacher.setUpdatedBy(SYSTEM_OPERATOR_ID);
        teacherMapper.updateTeacher(teacher);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTeacherAccount(Long id) {
        TeacherEntity teacher = teacherMapper.selectTeacherEntityById(id);
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }
        if (teacher.getUserId() == null) {
            throw new BusinessException("当前教师未绑定系统账号");
        }
        removeLinkedUser(teacher.getUserId());
        teacher.setUserId(null);
        teacher.setUpdatedBy(SYSTEM_OPERATOR_ID);
        teacherMapper.updateTeacher(teacher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importTeachers(List<TeacherCreateRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BusinessException("导入数据不能为空");
        }
        int count = 0;
        for (TeacherCreateRequest request : requests) {
            createTeacher(request);
            count++;
        }
        return count;
    }

    @Override
    public byte[] exportTeachers(TeacherQueryRequest request) {
        List<TeacherPageItemVO> list = teacherMapper.selectTeacherExportList(request);
        List<List<String>> rows = new ArrayList<>();
        for (TeacherPageItemVO item : list) {
            rows.add(List.of(
                valueOf(item.getTeacherNo()),
                valueOf(item.getTeacherName()),
                valueOf(item.getDeptName()),
                valueOf(item.getTitleName()),
                valueOf(item.getPhone()),
                valueOf(item.getEmail()),
                item.getStatus() != null && item.getStatus() == 1 ? "在职" : "离职",
                valueOf(item.getAccountUsername())
            ));
        }
        return CsvExportUtils.buildCsv(
            List.of("工号", "姓名", "部门", "职称", "手机号", "邮箱", "状态", "系统账号"),
            rows
        );
    }

    private void validateTeacherNo(String teacherNo, Long currentId) {
        TeacherEntity existing = teacherMapper.selectTeacherEntityByNo(teacherNo);
        if (existing != null && !existing.getId().equals(currentId)) {
            throw new BusinessException("工号已存在");
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
        if (user.getUserType() != TEACHER_USER_TYPE) {
            throw new BusinessException("绑定用户类型必须为教师");
        }
        TeacherEntity existing = teacherMapper.selectTeacherEntityByUserId(userId);
        if (existing != null && !existing.getId().equals(currentId)) {
            throw new BusinessException("该账号已绑定其他教师档案");
        }
    }

    private void validateDepartment(Long deptId) {
        if (deptId == null || departmentMapper.selectDepartmentById(deptId) == null) {
            throw new BusinessException("所属部门不存在");
        }
    }

    private void fillTeacherEntity(
        TeacherEntity entity,
        Long userId,
        String teacherNo,
        String teacherName,
        Integer gender,
        Long deptId,
        String titleName,
        String phone,
        String email,
        Integer status,
        String remark
    ) {
        entity.setUserId(userId);
        entity.setTeacherNo(teacherNo);
        entity.setTeacherName(teacherName);
        entity.setGender(gender);
        entity.setDeptId(deptId);
        entity.setTitleName(titleName);
        entity.setPhone(phone);
        entity.setEmail(email);
        entity.setStatus(status == null ? 1 : status);
        entity.setRemark(remark);
    }

    private void syncLinkedUser(TeacherEntity teacher) {
        if (teacher.getUserId() == null) {
            return;
        }
        UserEntity user = userMapper.selectUserEntityById(teacher.getUserId());
        if (user == null) {
            throw new BusinessException("绑定的用户不存在");
        }
        user.setRealName(teacher.getTeacherName());
        user.setUserType(TEACHER_USER_TYPE);
        user.setDeptId(teacher.getDeptId());
        user.setGender(teacher.getGender());
        user.setPhone(teacher.getPhone());
        user.setEmail(teacher.getEmail());
        user.setStatus(teacher.getStatus());
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
