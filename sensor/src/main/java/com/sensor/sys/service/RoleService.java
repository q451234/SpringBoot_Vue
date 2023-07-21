package com.sensor.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sensor.sys.Manager.RoleManager;
import com.sensor.sys.Manager.RoleMenuManager;
import com.sensor.sys.entity.Role;
import com.sensor.sys.entity.RoleMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@Slf4j
public class RoleService{

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RoleMenuManager roleMenuManager;


    public void addRole(Role role) {
        roleManager.insertRole(role);
        if(role.getMenuIdList() != null){
            for(Integer menuId : role.getMenuIdList()){
                roleMenuManager.insertRoleMenu(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }


    public Role getRoleById(Integer id) {
        Role role = roleManager.getRoleById(id);
        List<Integer> menuIdList = roleMenuManager.getMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        return role;
    }



    public void updateRole(Role role) {
        // 更新role表
        roleManager.updateRole(role);
        // 清除原有权限
        roleMenuManager.deleteByRoleId(role.getRoleId());
        //新增权限
        for (Integer menuId : role.getMenuIdList()) {
            roleMenuManager.insertRoleMenu(new RoleMenu(null,role.getRoleId(),menuId));
        }
    }


    public void deleteRoleById(Integer id) {
        roleManager.deleteRoleById(id);
        // 清除原有权限
        roleMenuManager.deleteByRoleId(id);
    }

    public List<Role> getALlRole(){
        return roleManager.getALlRole();
    }

    public Page<Role> getPage(String roleName, Long pageNo, Long pageSize){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName, roleName);
        wrapper.orderByDesc(Role::getRoleId);

        Page<Role> page = new Page<>(pageNo,pageSize);
        page = roleManager.getPage(page, wrapper);

        return page;
    }
}
