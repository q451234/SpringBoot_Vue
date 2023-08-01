package com.sensor.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sensor.common.Access;
import com.sensor.common.AccessLevel;
import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.entity.User;
import com.sensor.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户相关接口列表")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation("获取所有用户信息")
    @Access(level = AccessLevel.MANAGER)
    @GetMapping("/all")
    public Result<List<User>> getAllUser(){
        List<User> list = userService.getALlUser();
        return Result.success(list,"查询成功");
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){
        Map<String,Object> data = userService.login(user);
        if(data != null){
            if((int)data.get("status") == 0){
                log.info("用户被禁止登陆", user);
                return Result.fail(Constant.FAIL_CODE_1, "用户被禁止登陆");
            }
            return Result.success(data);
        }
        log.info("用户名或密码错误", user);
        return Result.fail(Constant.FAIL_CODE_2,"用户名或密码错误");
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<Map<String,Object>> getUserInfo(@RequestParam("token") String token, @RequestParam("refresh") boolean refresh){
        // 根据token获取用户信息，redis
        Map<String,Object> data = userService.getUserInfo(token, refresh);
        if(data != null){
            return Result.success(data);
        }
        log.info("登录信息无效，请重新登录");
        return Result.fail(Constant.FAIL_CODE_3,"登录信息无效，请重新登录");
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }

    @ApiOperation("用户搜索")
    @Access(level = AccessLevel.MANAGER)
    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                              @RequestParam(value = "phone",required = false) String phone,
                                              @RequestParam(value = "pageNo") Long pageNo,
                                              @RequestParam(value = "pageSize") Long pageSize){

        Page<User> page = userService.getPage(username, phone, pageNo, pageSize);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);

    }

    @ApiOperation("添加用户")
    @Access(level = AccessLevel.MANAGER)
    @PostMapping
    public Result<?> addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try{
            userService.addUser(user);
        }catch (Exception SQLIntegrityConstraintViolationException){
            log.info("用户名已存在", user);
            return Result.fail(Constant.FAIL_CODE_3, "用户名已存在");
        }

        return Result.success("新增用户成功");
    }

    @ApiOperation("修改用户信息")
    @Access(level = AccessLevel.MANAGER)
    @PutMapping
    public Result<?> updateUser(@RequestBody User user){
        if(user.getPassword() != null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.updateUser(user);
        return Result.success("修改用户成功");
    }

    @ApiOperation("个人修改用户信息")
    @PostMapping("/personal")
    public Result<?> updateUserPersonal(@RequestBody User user){
        if(user.getPassword() != null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.updateUserPersonal(user);
        return Result.success("修改信息成功");
    }

    @ApiOperation("查询用户")
    @Access(level = AccessLevel.MANAGER)
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    @ApiOperation("删除用户")
    @Access(level = AccessLevel.MANAGER)
    @DeleteMapping("/{id}")
    public Result<User> deleteUserById(@PathVariable("id") Integer id){
        userService.deleteUserById(id);
        return Result.success("删除用户成功");
    }

}
