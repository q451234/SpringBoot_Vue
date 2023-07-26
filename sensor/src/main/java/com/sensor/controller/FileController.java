package com.sensor.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.entity.SensorData;
import com.sensor.service.FileService;
import com.sensor.util.PautaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class FileController {
    @Autowired
    FileService fileService;
    @PostMapping("/upload")
    public Result<?> fileUpload(@RequestBody List<SensorData> sensorDataList) throws ParseException {
        if(sensorDataList.size() == 0){
            return Result.fail(Constant.FAIL_CODE_4,"请上传文件");
        }

        fileService.uploadData(sensorDataList);
        return Result.success("上传成功");
    }

}