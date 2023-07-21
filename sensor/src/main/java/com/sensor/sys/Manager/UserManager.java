package com.sensor.sys.Manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sensor.sys.entity.User;
import com.sensor.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class UserManager {

    @Autowired
    UserMapper userMapper;
    public User getByUserName(String userName){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userName);

        return userMapper.selectOne(wrapper);
    }

    @Transactional
    public void saveUser(User user){
        userMapper.insert(user);
    }

    public User getByUserId(Integer userId){
        return userMapper.selectById(userId);
    }

    @Transactional
    public void updateUser(User user){
        userMapper.updateById(user);
    }

    @Transactional
    public void deleteById(Integer id){
        userMapper.deleteById(id);
    }

    public List<User> getALlUser(){
        return userMapper.selectList(null);
    }

    public Page<User> getPage(Page<User> page, Wrapper<User> wrapper){
        return userMapper.selectPage(page,wrapper);
    }
}
