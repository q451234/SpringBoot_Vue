package com.sensor.sys.Manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sensor.sys.entity.RoleMenu;
import com.sensor.sys.mapper.RoleMapper;
import com.sensor.sys.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Configuration
public class RoleMenuManager {
    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Transactional
    public void insertRoleMenu(RoleMenu roleMenu){
        roleMenuMapper.insert(roleMenu);
    }

    public List<Integer> getMenuIdListByRoleId(Integer roleId){
        return roleMenuMapper.getMenuIdListByRoleId(roleId);
    }

    @Transactional
    public void deleteByRoleId(Integer id){
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        roleMenuMapper.delete(wrapper);
    }
}
