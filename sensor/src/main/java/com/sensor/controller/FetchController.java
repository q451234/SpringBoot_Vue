package com.sensor.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.sensor.common.Result;
import com.sensor.entity.Menu;
import com.sensor.entity.SensorData;
import com.sensor.mapper.FetchMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fetch")
public class FetchController {

    @Autowired
    FetchMapper fetchMapper;
    @GetMapping
    public Result<?> getAllMenu() throws ParseException {
        List<SensorData> sensorDataList = fetchMapper.fetchSensorList();

        char[] token = "XIpevgESCHb5kcGhmefA0L9EM80dXYwnz3R_lbl7usxoYEyZ2OXf0tyUimaC8BR-w4nxBWaSqd2Y1aWldkpvIQ==".toCharArray();
        String org = "CRDC";
        String bucket = "sensor";

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);

        // Write data
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(SensorData sensorData: sensorDataList){
            if(sensorData.getAd() != null){
                Date date = dateFormat.parse(sensorData.getClsj());
                sensorData.setTime(date.toInstant());
                writeApi.writeMeasurement( WritePrecision.MS, sensorData);
            }
        }
        return Result.success(sensorDataList);
    }
}
