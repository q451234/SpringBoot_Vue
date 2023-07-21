package com.sensor.sys.Manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sensor.sys.entity.UserRole;
import com.sensor.sys.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class UserRoleManager {

    @Autowired
    UserRoleMapper userRoleMapper;

    public List<String> getRoleNameListByUserId(Integer userId){
        return userRoleMapper.getRoleNameByUserId(userId);
    }

    @Transactional
    public void saveUserRole(UserRole userRole){
        userRoleMapper.insert(userRole);
    }

    public List<UserRole> getUserRoleListByUserId(Integer userId){
        LambdaQueryWrapper<UserRole> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(UserRole::getUserId, userId);
        return userRoleMapper.selectList(Wrapper);
    }

    @Transactional
    public void deleteByUserId(Integer userId){
        LambdaQueryWrapper<UserRole> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(UserRole::getUserId,userId);
        userRoleMapper.delete(Wrapper);
    }

}
