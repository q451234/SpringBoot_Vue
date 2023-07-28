package com.sensor.controller;

import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.service.SensorDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("传感器数据搜索")
    @GetMapping("/list")
    public Result<Map<String,Object>> getSensorDataList(@RequestParam(value = "projectName",required = false) String projectName,
                                                          @RequestParam(value = "cdId",required = false) String cdId,
                                                          @RequestParam(value = "dateStart",required = false) String dateStart,
                                                          @RequestParam(value = "dateEnd",required = false) String dateEnd,
                                                          @RequestParam(value = "field",required = false) String field,
                                                          @RequestParam(value = "pageNo") Long pageNo,
                                                          @RequestParam(value = "pageSize") Long pageSize,
                                                          @RequestParam(value = "flag") boolean flag){

        List<Map<String, Object>> sensorDataList = sensorDataService.getSensorDataList(projectName, cdId, dateStart, dateEnd, field, pageNo, pageSize, flag);

        Map<String,Object> data = new HashMap<>();
        if(flag){
            data.put("total", sensorDataList.size());
        }
        data.put("rows", sensorDataList);

        if(sensorDataList.size() == 0){
            return Result.fail(Constant.FAIL_CODE_4, "查询条件不匹配或数据为空");
        }
        return Result.success(data, "查询成功");
    }

    @ApiOperation("传感器数据绘制")
    @PostMapping("/draw")
    public Result<Map<String,Object>> getSensorDataDrawList(@RequestParam(value = "projectName") String projectName,
                                                        @RequestParam(value = "cdName") String cdName,
                                                        @RequestParam(value = "boxName") String boxName,
                                                        @RequestParam(value = "dateStart") String dateStart,
                                                        @RequestParam(value = "dateEnd") String dateEnd,
                                                        @RequestBody List<String> field){


        Map<String, Object> sensorDataList = sensorDataService.getSensorDataDrawList(projectName, boxName, cdName, dateStart, dateEnd, field);

        if(sensorDataList.get("date") == null){
            return Result.fail(Constant.FAIL_CODE_4, "数据被全部清洗");
        }
        return Result.success(sensorDataList, "查询成功");
    }
}
