package com.sensor.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sensor.entity.RoleData;
import com.sensor.entity.User;
import com.sensor.manager.*;
import com.sensor.entity.Role;
import com.sensor.entity.RoleMenu;
import com.sensor.mapper.UserMapper;
import com.sensor.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class RoleService{

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RoleMenuManager roleMenuManager;
    @Autowired
    private RoleDataManager roleDataManager;

    @Autowired
    private UserRoleManager userRoleManager;

    @Autowired
    private UserManager userManager;


    public void addRole(Role role) {
        roleManager.insertRole(role);
        if(role.getMenuIdList() != null){
            for(Integer menuId : role.getMenuIdList()){
                roleMenuManager.insertRoleMenu(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
        if(role.getProjectNameList() != null) {
            for (String projectName : role.getProjectNameList()) {
                String projectId = roleDataManager.getProjectIdByProjectName(projectName);
                roleDataManager.insertRoleData(new RoleData(null,role.getRoleId(),projectId,projectName));
            }
        }
    }


    public Role getRoleById(Integer id) {
        Role role = roleManager.getRoleById(id);
        List<Integer> menuIdList = roleMenuManager.getMenuIdListByRoleId(id);
        List<RoleData> roleDataList = roleDataManager.getRoleDataListByRoleId(id);
        role.setMenuIdList(menuIdList);
        role.setProjectNameList(roleDataList.stream().map(RoleData::getProjectName).collect(Collectors.toList()));
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

        // 清除原有数据权限
        roleDataManager.deleteByRoleId(role.getRoleId());
        //新增数据权限
        for (String projectName : role.getProjectNameList()) {
            String projectId = roleDataManager.getProjectIdByProjectName(projectName);
            roleDataManager.insertRoleData(new RoleData(null,role.getRoleId(),projectId,projectName));
        }
    }


    public void deleteRoleById(Integer id) {
        roleManager.deleteRoleById(id);
        // 清除原有权限
        roleMenuManager.deleteByRoleId(id);
        // 清除数据权限
        roleDataManager.deleteByRoleId(id);

        //对应角色用户状态设置为禁用
        List<Integer> userIdList = userRoleManager.getUserIdByRoleId(id);
        for(Integer userId : userIdList){
            User user = userManager.getByUserId(userId);
            user.setStatus(0);
            userManager.updateUser(user);
            userRoleManager.deleteByUserId(userId);
        }
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
