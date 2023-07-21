package com.sensor.sys.service;

import com.sensor.sys.Manager.MenuManger;
import com.sensor.sys.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    MenuManger menuManager;

    public List<Menu> getAllMenu() {

        List<Menu> menuList = menuManager.getMenuListByParentId(0);
        // 子菜单
        setMenuChildren(menuList);
        return menuList;
    }


    public List<Menu> getMenuListByUserId(Integer userId) {
        List<Menu> menuList = menuManager.getMenuListByUserId(userId, 0);
        // 子菜单
        setMenuChildrenByUserId(userId, menuList);
        return menuList;
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if (menuList != null) {
            for (Menu menu : menuList) {
                List<Menu> subMenuList = menuManager.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                // 递归
                setMenuChildrenByUserId(userId,subMenuList);
            }
        }
    }
    private void setMenuChildren(List<Menu> menuList) {
        if(menuList != null) {
            for (Menu menu:menuList) {
                List<Menu> subMenuList = menuManager.getMenuListByParentId(menu.getMenuId());
                menu.setChildren(subMenuList);
                // 递归
                setMenuChildren(subMenuList);
            }
        }
    }

}
