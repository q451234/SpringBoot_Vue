package com.sensor.controller;

import com.sensor.common.Result;
import com.sensor.entity.Role;
import com.sensor.entity.RoleData;
import com.sensor.service.RoleDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "数据权限接口列表")
@Slf4j
@RestController
@RequestMapping("/roleData")
public class RoleDataController {

    @Autowired
    private RoleDataService roleDataService;

    @ApiOperation("获取所有数据权限列表")
    @GetMapping
    public Result<?> getAllRoleData(){
        List<RoleData> roleDataList = roleDataService.getAllProject();

        List<String> projectNameList = roleDataList.stream().map(RoleData::getProjectName).collect(Collectors.toList());

        return Result.success(projectNameList);
    }
}
