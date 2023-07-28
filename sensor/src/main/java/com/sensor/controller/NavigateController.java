package com.sensor.controller;

import com.sensor.common.Result;
import com.sensor.service.NavigateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "搜索栏相关接口列表")
@Slf4j
@RestController
@RequestMapping("/navigate")
public class NavigateController {
    @Autowired
    private NavigateService navigateService;

    @ApiOperation("获取所有导航栏")
    @GetMapping
    public Result<?> getAllNavigate(){
        Map<String, Map<String, List<String>>> navigateMap =  navigateService.getNavigateMap();
        return Result.success(navigateMap);
    }

}

