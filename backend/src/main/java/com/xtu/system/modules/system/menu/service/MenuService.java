package com.xtu.system.modules.system.menu.service;

import com.xtu.system.modules.system.menu.dto.MenuCreateRequest;
import com.xtu.system.modules.system.menu.dto.MenuUpdateRequest;
import com.xtu.system.modules.system.menu.vo.MenuTreeVO;

import java.util.List;

public interface MenuService {

    List<MenuTreeVO> getMenuTree();

    MenuTreeVO getMenuDetail(Long id);

    Long createMenu(MenuCreateRequest request);

    void updateMenu(Long id, MenuUpdateRequest request);

    void deleteMenu(Long id);
}
