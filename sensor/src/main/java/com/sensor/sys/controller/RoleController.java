package com.sensor.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sensor.common.Result;
import com.sensor.sys.entity.Role;
import com.sensor.sys.service.IRoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laocai
 * @since 2023-02-07
 */
@Api(tags = "角色相关接口列表")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;


    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return Result.success(roleList,"查询成功");
    }

    @GetMapping("/list")
    public Result<Map<String,Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);
        wrapper.orderByDesc(Role::getRoleId);

        Page<Role> page = new Page<>(pageNo,pageSize);
        roleService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);

    }

    @PostMapping
    public Result<?> addRole(@RequestBody Role role){
        try{
            roleService.addRole(role);
        }catch (Exception SQLIntegrityConstraintViolationException){
            return Result.fail(20003, "角色名已存在");
        }

        return Result.success("新增角色成功");
    }

    @PutMapping
    public Result<?> updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success("修改用户成功");
    }

    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @DeleteMapping("/{id}")
    public Result<Role> deleteRoleById(@PathVariable("id") Integer id){
        roleService.deleteRoleById(id);
        return Result.success("删除用户成功");
    }
}
