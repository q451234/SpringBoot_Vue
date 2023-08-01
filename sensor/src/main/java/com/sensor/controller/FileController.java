package com.sensor.controller;

import com.sensor.common.Access;
import com.sensor.common.AccessLevel;
import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.entity.SensorData;
import com.sensor.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@Api(tags = "文件上传相关接口列表")
@RestController
@Slf4j
public class FileController {
    @Autowired
    FileService fileService;
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    @Access(level = AccessLevel.NORMAL)
    public Result<?> fileUpload(@RequestBody List<SensorData> sensorDataList) throws ParseException {
        if(sensorDataList.size() == 0){
            return Result.fail(Constant.FAIL_CODE_4,"请上传文件");
        }

        fileService.uploadData(sensorDataList);
        return Result.success("上传成功");
    }

}