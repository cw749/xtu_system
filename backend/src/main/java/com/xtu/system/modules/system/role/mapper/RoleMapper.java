package com.xtu.system.modules.system.role.mapper;

import com.xtu.system.modules.system.role.dto.RoleQueryRequest;
import com.xtu.system.modules.system.role.entity.RoleEntity;
import com.xtu.system.modules.system.role.vo.RoleDetailVO;
import com.xtu.system.modules.system.role.vo.RoleOptionVO;
import com.xtu.system.modules.system.role.vo.RolePageItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<RolePageItemVO> selectRolePage(RoleQueryRequest request);

    long countRolePage(RoleQueryRequest request);

    RoleDetailVO selectRoleDetailById(@Param("id") Long id);

    RoleEntity selectRoleEntityById(@Param("id") Long id);

    RoleEntity selectRoleEntityByCode(@Param("roleCode") String roleCode);

    int insertRole(RoleEntity entity);

    int updateRole(RoleEntity entity);

    int logicDeleteRole(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    int updateRoleStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedBy") Long updatedBy);

    List<RoleOptionVO> selectRoleOptions();

    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    int deleteRoleMenusByRoleId(@Param("roleId") Long roleId);

    int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds, @Param("operatorId") Long operatorId);

    long countUserBindingsByRoleId(@Param("roleId") Long roleId);
}
