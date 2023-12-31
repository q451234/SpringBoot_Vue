package com.sensor.controller;

import com.sensor.common.*;
import com.sensor.service.SensorDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "传感器数据相关接口列表")
@Slf4j
@RestController
@RequestMapping("/sensor")
public class SensorDataController {

    @Autowired
    SensorDataService sensorDataService;
    @Autowired
    DataAuthorize dataAuthorize;

    @ApiOperation("传感器数据搜索")
    @Access(level = AccessLevel.NORMAL)
    @GetMapping("/list")
    public Result<Map<String,Object>> getSensorDataList(@RequestParam(value = "projectName",required = false) String projectName,
                                                        @RequestParam(value = "cdName",required = false) String cdName,
                                                        @RequestParam(value = "boxName",required = false) String boxName,
                                                        @RequestParam(value = "dateStart",required = false) String dateStart,
                                                        @RequestParam(value = "dateEnd",required = false) String dateEnd,
                                                        @RequestParam(value = "field",required = false) String field,
                                                        @RequestParam(value = "pageNo") Long pageNo,
                                                        @RequestParam(value = "pageSize") Long pageSize,
                                                        @RequestParam(value = "flag") boolean flag,
                                                        HttpServletRequest request){

        List<String> projectIdAuthorizeList = dataAuthorize.getProjectIdAuthorize(request);
        if(!projectIdAuthorizeList.contains(projectName)){
            return Result.fail(Constant.FAIL_CODE_AUTHORIZED, "用户无访问数据权限");
        }

        List<Map<String, Object>> sensorDataList = sensorDataService.getSensorDataList(projectName, boxName, cdName, dateStart, dateEnd, field, pageNo, pageSize, flag);

        Map<String,Object> data = new HashMap<>();
        if(flag){
            data.put("total", sensorDataList.size());
        }
        data.put("rows", sensorDataList);

        if(sensorDataList.size() == 0){
            return Result.fail(Constant.FAIL_CODE_4, "查询条件不匹配或无可用数据");
        }
        return Result.success(data, "查询成功");
    }

    @ApiOperation("传感器过程数据绘制")
    @Access(level = AccessLevel.NORMAL)
    @PostMapping("/drawProcess")
    public Result<Map<String,Object>> getSensorDataDrawList(@RequestParam(value = "projectName") String projectName,
                                                            @RequestParam(value = "cdName") String cdName,
                                                            @RequestParam(value = "boxName") String boxName,
                                                            @RequestParam(value = "dateStart") String dateStart,
                                                            @RequestParam(value = "dateEnd") String dateEnd,
                                                            @RequestBody List<String> field,
                                                            HttpServletRequest request){

        List<String> projectIdAuthorizeList = dataAuthorize.getProjectIdAuthorize(request);
        if(!projectIdAuthorizeList.contains(projectName)){
            return Result.fail(Constant.FAIL_CODE_AUTHORIZED, "用户无访问数据权限");
        }

        Map<String, Object> sensorDataList = sensorDataService.getSensorDataDrawProcessList(projectName, boxName, cdName, dateStart, dateEnd, field);

        if(sensorDataList.get("date") == null){
            return Result.fail(Constant.FAIL_CODE_4, "查询条件不匹配或无可用数据");
        }
        return Result.success(sensorDataList, "查询成功");
    }

    @ApiOperation("传感器分布数据绘制")
    @Access(level = AccessLevel.NORMAL)
    @GetMapping("/drawDistribution")
    public Result<Map<String,Object>> getSensorDataDrawList(@RequestParam(value = "projectName") String projectName,
                                                            @RequestParam(value = "sensorType") String sensorType,
                                                            @RequestParam(value = "boxName") String boxName,
                                                            @RequestParam(value = "dateStart") String dateStart,
                                                            @RequestParam(value = "dateEnd") String dateEnd,
                                                            @RequestParam(value = "field") String field,
                                                            HttpServletRequest request){

        List<String> projectIdAuthorizeList = dataAuthorize.getProjectIdAuthorize(request);
        if(!projectIdAuthorizeList.contains(projectName)){
            return Result.fail(Constant.FAIL_CODE_AUTHORIZED, "用户无访问数据权限");
        }

        Map<String, Object> sensorDataList;
        try{
            sensorDataList = sensorDataService.getSensorDataDrawDistributionList(projectName, boxName, sensorType, dateStart, dateEnd, field);
        }catch (IndexOutOfBoundsException e){
            return Result.fail(Constant.FAIL_CODE_4, "查询条件不匹配或无可用数据");
        }
        return Result.success(sensorDataList, "查询成功");
    }
}
