package com.sensor.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.entity.SensorData;
import com.sensor.mapper.FetchMapper;
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

@Slf4j
@RestController
@RequestMapping("/fetch")
public class FetchController {

    @Autowired
    FetchMapper fetchMapper;
    @GetMapping
    public Result<?> getAllMenu() throws ParseException {
        List<SensorData> sensorDataList = fetchMapper.fetchSensorList();

        char[] token = Constant.INFLUXDB_TOKEN.toCharArray();
        String org = Constant.INFLUXDB_ORG;
        String bucket = "SensorData";

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(Constant.INFLUXDB_URL, token, org, bucket);

        PautaUtil.removeException(sensorDataList);

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
