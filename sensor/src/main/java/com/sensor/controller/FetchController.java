package com.sensor.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.entity.SensorData;
import com.sensor.mapper.FetchMapper;
import com.sensor.util.FluxUtil;
import com.sensor.util.PautaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//转移mysql数据到influxdb
@Slf4j
@RestController
@RequestMapping("/fetch")
public class FetchController {

    @Autowired
    FetchMapper fetchMapper;
    @GetMapping
    public Result<?> getAllMenu() throws ParseException {
        List<SensorData> sensorDataList = fetchMapper.fetchSensorList();

        String bucket = "sensorFilter";

        InfluxDBClient influxDBClient = FluxUtil.createInfluxClient(bucket);

        System.out.println(sensorDataList.size());
        sensorDataList = sensorDataList.stream().filter(sensorData ->
                sensorData.getSubstand() <= Double.parseDouble(sensorData.getLMax()) &&
                        sensorData.getSubstand() >= Double.parseDouble(sensorData.getLMin()) &&
                        sensorData.getMData() <= Double.parseDouble(sensorData.getLMax()) &&
                        sensorData.getMData() >= Double.parseDouble(sensorData.getLMin()) &&
                        sensorData.getCalculatedata() <= Double.parseDouble(sensorData.getLMax()) &&
                        sensorData.getCalculatedata() >= Double.parseDouble(sensorData.getLMin())

        ).collect(Collectors.toList());
        System.out.println(sensorDataList.size());
        PautaUtil.removeException(sensorDataList);
        System.out.println(sensorDataList.size());
        // Write data
        WriteApi writeApi = influxDBClient.makeWriteApi();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(SensorData sensorData: sensorDataList){
            Date date = dateFormat.parse(sensorData.getClsj());
            sensorData.setTime(date.toInstant());
            writeApi.writeMeasurement( WritePrecision.MS, sensorData);
        }

        writeApi.flush();
        writeApi.close();

        return Result.success();
    }
}
