package com.sensor.controller;

import com.sensor.common.AuthorizedException;
import com.sensor.common.Constant;
import com.sensor.common.DataAuthorize;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "搜索栏相关接口列表")
@Slf4j
@RestController
@RequestMapping("/navigate")
public class NavigateController {
    @Autowired
    private NavigateService navigateService;

    @Autowired
    DataAuthorize dataAuthorize;

    @ApiOperation("获取过程导航栏")
    @GetMapping("/process")
    public Result<?> getProcessNavigate(HttpServletRequest request){
        List<String> projectIdAuthorizeList = dataAuthorize.getProjectIdAuthorize(request);
        Map<String, Object> navigateMap;
        try {
            navigateMap = navigateService.getNavigateMapProcess(projectIdAuthorizeList);
        } catch (AuthorizedException e) {
            return Result.fail(Constant.FAIL_CODE_AUTHORIZED, "用户无访问数据权限");
        }

        return Result.success(navigateMap);
    }

    @ApiOperation("获取分布导航栏")
    @GetMapping("/distribution")
    public Result<?> getDistributionNavigate(HttpServletRequest request){
        List<String> projectIdAuthorizeList = dataAuthorize.getProjectIdAuthorize(request);
        Map<String, Object> navigateMap;
        try {
            navigateMap = navigateService.getNavigateMapDistribution(projectIdAuthorizeList);
        } catch (AuthorizedException e) {
            return Result.fail(Constant.FAIL_CODE_AUTHORIZED, "用户无访问数据权限");
        }

        return Result.success(navigateMap);
    }

    @ApiOperation("获取测斜仪导航栏")
    @GetMapping("/inclinometer")
    public Result<?> getInclinometerNavigate(@RequestParam(value = "sensorType") String sensorType,
                                             HttpServletRequest request){

        List<String> projectIdAuthorizeList = dataAuthorize.getProjectIdAuthorize(request);
        Map<String, Object> navigateMap;
        try {
            navigateMap = navigateService.getNavigateMapInclinometer(sensorType, projectIdAuthorizeList);
        } catch (AuthorizedException e) {
            return Result.fail(Constant.FAIL_CODE_AUTHORIZED, "用户无访问数据权限");
        }

        return Result.success(navigateMap);
    }
}

