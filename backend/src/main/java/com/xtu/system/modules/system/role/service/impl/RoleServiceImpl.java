package com.xtu.system.modules.system.role.service.impl;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.modules.system.role.dto.RoleCreateRequest;
import com.xtu.system.modules.system.role.dto.RoleQueryRequest;
import com.xtu.system.modules.system.role.dto.RoleUpdateRequest;
import com.xtu.system.modules.system.role.entity.RoleEntity;
import com.xtu.system.modules.system.role.mapper.RoleMapper;
import com.xtu.system.modules.system.role.service.RoleService;
import com.xtu.system.modules.system.role.vo.RoleDetailVO;
import com.xtu.system.modules.system.role.vo.RoleOptionVO;
import com.xtu.system.modules.system.role.vo.RolePageItemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private static final long SYSTEM_OPERATOR_ID = 1L;

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public PageResponse<RolePageItemVO> getRolePage(RoleQueryRequest request) {
        List<RolePageItemVO> list = roleMapper.selectRolePage(request);
        long total = roleMapper.countRolePage(request);
        return PageResponse.of(list, request.getPageNum(), request.getPageSize(), total);
    }

    @Override
    public RoleDetailVO getRoleDetail(Long id) {
        RoleDetailVO detail = roleMapper.selectRoleDetailById(id);
        if (detail == null) {
            throw new BusinessException("角色不存在");
        }
        detail.setMenuIds(roleMapper.selectMenuIdsByRoleId(id));
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateRequest request) {
        validateRoleCode(request.getRoleCode(), null);

        RoleEntity entity = new RoleEntity();
        entity.setRoleCode(request.getRoleCode());
        entity.setRoleName(request.getRoleName());
        entity.setDataScope(request.getDataScope());
        entity.setStatus(request.getStatus());
        entity.setRemark(request.getRemark());
        entity.setCreatedBy(SYSTEM_OPERATOR_ID);
        entity.setUpdatedBy(SYSTEM_OPERATOR_ID);
        roleMapper.insertRole(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long id, RoleUpdateRequest request) {
        RoleEntity existing = roleMapper.selectRoleEntityById(id);
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }
        validateRoleCode(request.getRoleCode(), id);
        existing.setRoleCode(request.getRoleCode());
        existing.setRoleName(request.getRoleName());
        existing.setDataScope(request.getDataScope());
        existing.setStatus(request.getStatus());
        existing.setRemark(request.getRemark());
        existing.setUpdatedBy(SYSTEM_OPERATOR_ID);
        roleMapper.updateRole(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        RoleEntity existing = roleMapper.selectRoleEntityById(id);
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }
        if (roleMapper.countUserBindingsByRoleId(id) > 0) {
            throw new BusinessException("该角色已分配给用户，不能删除");
        }
        roleMapper.logicDeleteRole(id, SYSTEM_OPERATOR_ID);
        roleMapper.deleteRoleMenusByRoleId(id);
    }

    @Override
    public void updateRoleStatus(Long id, Integer status) {
        if (roleMapper.updateRoleStatus(id, status, SYSTEM_OPERATOR_ID) == 0) {
            throw new BusinessException("角色不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenus(Long id, List<Long> menuIds) {
        RoleEntity existing = roleMapper.selectRoleEntityById(id);
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }
        roleMapper.deleteRoleMenusByRoleId(id);
        if (menuIds != null && !menuIds.isEmpty()) {
            roleMapper.insertRoleMenus(id, menuIds, SYSTEM_OPERATOR_ID);
        }
    }

    @Override
    public List<RoleOptionVO> getRoleOptions() {
        return roleMapper.selectRoleOptions();
    }

    private void validateRoleCode(String roleCode, Long currentId) {
        RoleEntity existing = roleMapper.selectRoleEntityByCode(roleCode);
        if (existing != null && !existing.getId().equals(currentId)) {
            throw new BusinessException("角色编码已存在");
        }
    }
}
