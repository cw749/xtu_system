package com.xtu.system.modules.system.menu.service.impl;

import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.modules.system.menu.dto.MenuCreateRequest;
import com.xtu.system.modules.system.menu.dto.MenuUpdateRequest;
import com.xtu.system.modules.system.menu.entity.MenuEntity;
import com.xtu.system.modules.system.menu.mapper.MenuMapper;
import com.xtu.system.modules.system.menu.service.MenuService;
import com.xtu.system.modules.system.menu.vo.MenuTreeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    private static final long SYSTEM_OPERATOR_ID = 1L;

    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public List<MenuTreeVO> getMenuTree() {
        return buildMenuTree(menuMapper.selectAllMenus());
    }

    @Override
    public MenuTreeVO getMenuDetail(Long id) {
        MenuEntity entity = menuMapper.selectMenuById(id);
        if (entity == null) {
            throw new BusinessException("菜单不存在");
        }
        return toTreeNode(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMenu(MenuCreateRequest request) {
        validateParentMenu(request.getParentId(), null);
        MenuEntity entity = new MenuEntity();
        fillMenuEntity(entity, request.getParentId(), request.getMenuName(), request.getMenuType(), request.getRoutePath(),
            request.getComponentPath(), request.getPermissionCode(), request.getIcon(), request.getSortNo(), request.getVisible(),
            request.getStatus(), request.getRemark());
        entity.setCreatedBy(SYSTEM_OPERATOR_ID);
        entity.setUpdatedBy(SYSTEM_OPERATOR_ID);
        menuMapper.insertMenu(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Long id, MenuUpdateRequest request) {
        MenuEntity existing = menuMapper.selectMenuById(id);
        if (existing == null) {
            throw new BusinessException("菜单不存在");
        }
        validateParentMenu(request.getParentId(), id);
        fillMenuEntity(existing, request.getParentId(), request.getMenuName(), request.getMenuType(), request.getRoutePath(),
            request.getComponentPath(), request.getPermissionCode(), request.getIcon(), request.getSortNo(), request.getVisible(),
            request.getStatus(), request.getRemark());
        existing.setUpdatedBy(SYSTEM_OPERATOR_ID);
        menuMapper.updateMenu(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        MenuEntity existing = menuMapper.selectMenuById(id);
        if (existing == null) {
            throw new BusinessException("菜单不存在");
        }
        if (menuMapper.countChildrenByParentId(id) > 0) {
            throw new BusinessException("请先删除下级菜单");
        }
        if (menuMapper.countRoleBindingsByMenuId(id) > 0) {
            throw new BusinessException("该菜单已分配给角色，不能删除");
        }
        menuMapper.logicDeleteMenu(id, SYSTEM_OPERATOR_ID);
    }

    private void fillMenuEntity(
        MenuEntity entity,
        Long parentId,
        String menuName,
        String menuType,
        String routePath,
        String componentPath,
        String permissionCode,
        String icon,
        Integer sortNo,
        Integer visible,
        Integer status,
        String remark
    ) {
        entity.setParentId(parentId == null ? 0L : parentId);
        entity.setMenuName(menuName);
        entity.setMenuType(menuType);
        entity.setRoutePath(routePath);
        entity.setComponentPath(componentPath);
        entity.setPermissionCode(permissionCode);
        entity.setIcon(icon);
        entity.setSortNo(sortNo == null ? 0 : sortNo);
        entity.setVisible(visible == null ? 1 : visible);
        entity.setStatus(status == null ? 1 : status);
        entity.setRemark(remark);
    }

    private void validateParentMenu(Long parentId, Long currentId) {
        Long normalizedParentId = parentId == null ? 0L : parentId;
        if (currentId != null && normalizedParentId.equals(currentId)) {
            throw new BusinessException("父级菜单不能选择自己");
        }
        if (normalizedParentId != 0 && menuMapper.selectMenuById(normalizedParentId) == null) {
            throw new BusinessException("父级菜单不存在");
        }
        if (currentId != null && normalizedParentId != 0) {
            ensureNoMenuCycle(currentId, normalizedParentId);
        }
    }

    private void ensureNoMenuCycle(Long currentId, Long parentId) {
        Map<Long, Long> parentMap = new HashMap<>();
        for (MenuEntity entity : menuMapper.selectAllMenus()) {
            parentMap.put(entity.getId(), entity.getParentId());
        }

        Long cursor = parentId;
        while (cursor != null && cursor != 0) {
            if (cursor.equals(currentId)) {
                throw new BusinessException("父级菜单不能选择当前菜单的下级节点");
            }
            cursor = parentMap.get(cursor);
        }
    }

    private List<MenuTreeVO> buildMenuTree(List<MenuEntity> entities) {
        Map<Long, MenuTreeVO> nodeMap = new LinkedHashMap<>();
        List<MenuTreeVO> roots = new ArrayList<>();

        for (MenuEntity entity : entities) {
            nodeMap.put(entity.getId(), toTreeNode(entity));
        }

        for (MenuEntity entity : entities) {
            MenuTreeVO node = nodeMap.get(entity.getId());
            if (entity.getParentId() == null || entity.getParentId() == 0) {
                roots.add(node);
                continue;
            }
            MenuTreeVO parent = nodeMap.get(entity.getParentId());
            if (parent == null) {
                roots.add(node);
            } else {
                parent.getChildren().add(node);
            }
        }
        return roots;
    }

    private MenuTreeVO toTreeNode(MenuEntity entity) {
        MenuTreeVO node = new MenuTreeVO();
        node.setId(entity.getId());
        node.setParentId(entity.getParentId());
        node.setMenuName(entity.getMenuName());
        node.setMenuType(entity.getMenuType());
        node.setRoutePath(entity.getRoutePath());
        node.setComponentPath(entity.getComponentPath());
        node.setPermissionCode(entity.getPermissionCode());
        node.setIcon(entity.getIcon());
        node.setSortNo(entity.getSortNo());
        node.setVisible(entity.getVisible());
        node.setStatus(entity.getStatus());
        node.setRemark(entity.getRemark());
        return node;
    }
}
