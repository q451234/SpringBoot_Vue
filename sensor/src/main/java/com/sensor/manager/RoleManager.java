package com.sensor.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sensor.entity.Role;
import com.sensor.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class RoleManager {
    @Autowired
    RoleMapper roleMapper;

    @Transactional
    public void insertRole(Role role){
        roleMapper.insert(role);
    }

    public Role getRoleById(Integer id){
        return roleMapper.selectById(id);
    }

    @Transactional
    public void updateRole(Role role){
        roleMapper.updateById(role);
    }

    @Transactional
    public void deleteRoleById(Integer id){
        roleMapper.deleteById(id);
    }

    public List<Role> getALlRole(){
        return roleMapper.selectList(null);
    }

    public Page<Role> getPage(Page<Role> page, Wrapper<Role> wrapper){
        return roleMapper.selectPage(page,wrapper);
    }
}
