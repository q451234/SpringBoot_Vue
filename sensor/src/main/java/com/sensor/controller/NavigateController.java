package com.sensor.controller;

import com.sensor.common.Result;
import com.sensor.service.NavigateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @ApiOperation("获取过程导航栏")
    @GetMapping("/process")
    public Result<?> getProcessNavigate(){
        Map<String, Map<String, List<String>>> navigateMap =  navigateService.getNavigateMapProcess();
        return Result.success(navigateMap);
    }

    @ApiOperation("获取分布导航栏")
    @GetMapping("/distribution")
    public Result<?> getDistributionNavigate(){
        Map<String, Map<String, List<String>>> navigateMap =  navigateService.getNavigateMapDistribution();
        return Result.success(navigateMap);
    }

    @ApiOperation("获取测斜仪导航栏")
    @GetMapping("/inclinometer")
    public Result<?> getInclinometerNavigate(@RequestParam(value = "sensorType") String sensorType){
        Map<String, List<String>> navigateMap =  navigateService.getNavigateMapInclinometer(sensorType);
        return Result.success(navigateMap);
    }
}

