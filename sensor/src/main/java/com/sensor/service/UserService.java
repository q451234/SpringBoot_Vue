package com.sensor.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sensor.manager.UserManager;
import com.sensor.manager.UserRoleManager;
import com.sensor.entity.Menu;
import com.sensor.entity.User;
import com.sensor.entity.UserRole;
import com.sensor.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserService{

    @Autowired
    private UserRoleManager userRoleManager;

    @Autowired
    private UserManager userManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuService menuService;
    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> login(User user) {
        // 根据用户名查询
        User loginUser = userManager.getByUserName(user.getUsername());
        // 结果不为空，并且密码和传入密码匹配，则生成token，并将用户信息存入redis
        if(loginUser != null ){
            if(passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){
                loginUser.setPassword(null);

                String key = jwtUtil.createToken(loginUser);
                // 返回数据
                Map<String, Object> data = new HashMap<>();
                data.put("token",key);
                data.put("status", loginUser.getStatus());
                return data;
            }
        }
        return null;
    }


    public Map<String, Object> getUserInfo(String token, boolean refresh) {

        User loginUser = null;
        try{
            loginUser = jwtUtil.parseToken(token, User.class);
        }catch (Exception e){
            e.printStackTrace();
            log.info("JWT解析失败");
        }

        if(refresh){
            loginUser = userManager.getByUserId(loginUser.getId());
        }

        if(loginUser != null){
            Map<String, Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());
            data.put("phone", loginUser.getPhone());
            data.put("email", loginUser.getEmail());
            data.put("id", loginUser.getId());

            // 角色
            List<String> roleList = userRoleManager.getRoleNameListByUserId(loginUser.getId());
            data.put("roles",roleList);

            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList", menuList);
            return data;
        }
        return null;
    }


    public void logout(String token) {
    }


    public void addUser(User user) {
        userManager.saveUser(user);
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList != null){
            for(Integer roleId : roleIdList){
                userRoleManager.saveUserRole(new UserRole(null, user.getId(), roleId));
            }
        }
    }


    public User getUserById(Integer id) {
        User user = userManager.getByUserId(id);

        List<UserRole> userRoleList = userRoleManager.getUserRoleListByUserId(id);

        List<Integer> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        user.setRoleIdList(roleIdList);

        return user;
    }


    public void updateUser(User user) {
        // 更新用户表
        userManager.updateUser(user);
        // 清除原有角色
        userRoleManager.deleteByUserId(user.getId());
        // 设置新的角色
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleManager.saveUserRole(new UserRole(null,user.getId(),roleId));
            }
        }
    }

    public void updateUserPersonal(User user){
        userManager.updateUser(user);
    }

    public void deleteUserById(Integer id) {
        userManager.deleteById(id);
        // 清除原有角色
        userRoleManager.deleteByUserId(id);
    }


    public List<User> getALlUser(){
        return userManager.getALlUser();
    }

    public Page<User> getPage(String username, String phone, Long pageNo, Long pageSize){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
        wrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
        wrapper.orderByDesc(User::getId);

        Page<User> page = new Page<>(pageNo,pageSize);
        page = userManager.getPage(page, wrapper);

        return page;
    }
}
