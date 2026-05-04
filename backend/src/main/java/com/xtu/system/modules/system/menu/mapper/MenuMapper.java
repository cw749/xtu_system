package com.xtu.system.modules.system.menu.mapper;

import com.xtu.system.modules.system.menu.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<MenuEntity> selectAllMenus();

    List<MenuEntity> selectMenusByUserId(@Param("userId") Long userId);

    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

    MenuEntity selectMenuById(@Param("id") Long id);

    int insertMenu(MenuEntity entity);

    int updateMenu(MenuEntity entity);

    int logicDeleteMenu(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    long countChildrenByParentId(@Param("parentId") Long parentId);

    long countRoleBindingsByMenuId(@Param("menuId") Long menuId);
}
