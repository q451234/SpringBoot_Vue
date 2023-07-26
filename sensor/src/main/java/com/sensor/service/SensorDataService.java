package com.sensor.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxColumn;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.sensor.common.Constant;
import com.sensor.util.FluxUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private static String[] checkName = new String[]{"ad", "calculatedataName", "dataName", "substandName", "temperature"};

    @ApiOperation("传感器数据搜索")
    @GetMapping("/list")
    public List<Map<String, Object>> getSensorDataList(String projectName, String cdId, String dateStart,
                                                 String dateEnd, String field, Long pageNo, Long pageSize, boolean flag){

        String bucket = "SensorData";

        // Flux
        String flux = String.format("from(bucket: \"SensorData\")\n" +
                "  |> range(start: %s, stop: %s)\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\n" +
                "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")\n" +
                "  |> filter(fn: (r) => r[\"cdId\"] == \"%s\")\n", dateStart, dateEnd, projectName, field, cdId);

        if(!flag){
            flux += String.format("  |> limit(n:%s, offset: %s)", pageSize, (pageNo - 1) * pageSize);
        }

        InfluxDBClient influxDBClient = FluxUtil.createInfluxClient(bucket);
        QueryApi queryApi = influxDBClient.getQueryApi();

        // Synchronous query
        List<FluxTable> tables = queryApi.query(flux);
        List<Map<String, Object>> sensorDataList = new ArrayList<>();

        for (FluxTable fluxTable : tables) {
            List<FluxRecord> fluxRecordList = fluxTable.getRecords();
            List<FluxColumn> fluxColumnList = fluxTable.getColumns();

            for (int i = 0; i < fluxRecordList.size(); i++) {
                Map<String, Object> sensorData = new HashMap<>();
                Map<String, Object> fieldNameMap = new HashMap<>();

                for(FluxColumn fluxColumn: fluxColumnList){
                    String key = fluxColumn.getLabel();
                    if(!Arrays.asList(checkName).contains(key)){
                        sensorData.put(key, fluxRecordList.get(i).getValueByKey(key));
                    }else{
                        fieldNameMap.put(key, fluxRecordList.get(i).getValueByKey(key));
                    }
                }

                //获取搜索字段名称
                String fieldName = getFieldName(sensorData.get("_field").toString(), fieldNameMap);
                sensorData.put("_field", fieldName);
                sensorDataList.add(sensorData);

                //时间格式转换
                Instant instant = (Instant) sensorData.get("_time");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = dateFormat.format(new Date(instant.toEpochMilli()));
                sensorData.put("_time", date);
            }
        }

        return sensorDataList;
    }


    public String getFieldName(String field, Map<String, Object> sensorData){
        switch (field){
            case "ad" : return "AD值";
            case "temperature" : return "温度值";
            case "mData" : return sensorData.get("dataName").toString();
            case "calculatedata" : return sensorData.get("calculatedataName").toString();
            case "substand" : return sensorData.get("substandName").toString();
            default: return field;
        }
    }

    public Map<String, Object> getSensorDataDrawList(String projectName, String cdId, String dateStart, String dateEnd, String field) {
        String bucket = "SensorData";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Flux
        String flux = String.format("from(bucket: \"SensorData\")\n" +
                "  |> range(start: %s, stop: %s)\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"%s\")\n" +
                "  |> filter(fn: (r) => r[\"_field\"] == \"%s\")\n" +
                "  |> filter(fn: (r) => r[\"cdId\"] == \"%s\")\n", dateStart, dateEnd, projectName, field, cdId);

        InfluxDBClient influxDBClient = FluxUtil.createInfluxClient(bucket);
        QueryApi queryApi = influxDBClient.getQueryApi();

        // Synchronous query
        List<FluxTable> tables = queryApi.query(flux);

        Map<String, Object> sensorDataList = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Double> valueList = new ArrayList<>();

        for (FluxTable fluxTable : tables) {
            List<FluxRecord> fluxRecordList = fluxTable.getRecords();
            List<FluxColumn> fluxColumnList = fluxTable.getColumns();
            HashMap<String, Object> fieldNameMap = new HashMap<>();

            for(FluxColumn fluxColumn: fluxColumnList){
                String key = fluxColumn.getLabel();
                if(Arrays.asList(checkName).contains(key)){
                    fieldNameMap.put(key, fluxRecordList.get(0).getValueByKey(key));
                }
                if(key.equals("chuanganqileixing")){
                    String input = (String) fluxRecordList.get(0).getValueByKey("chuanganqileixing");
                    if(!field.equals("ad") && !field.equals("temperature")){
                        sensorDataList.put("unit", input.substring(input.indexOf("(") + 1 ,input.indexOf(")")));
                    }else{
                        sensorDataList.put("unit", " ");
                    }
                }
            }

            Instant instantStart = (Instant) fluxRecordList.get(0).getValueByKey("_start");
            dateList.add(dateFormat.format(new Date(instantStart.toEpochMilli())));

            String fieldName = getFieldName((String) Objects.requireNonNull(fluxRecordList.get(0).getValueByKey("_field")), fieldNameMap);

            for (FluxRecord fluxRecord : fluxRecordList) {
                //获取搜索字段名称

                Double value = (Double) fluxRecord.getValueByKey("_value");
                valueList.add(value);

                //时间格式转换
                Instant instant = (Instant) fluxRecord.getValueByKey("_time");
                String time = dateFormat.format(new Date(instant.toEpochMilli()));
                dateList.add(time);
            }


            Double distance = Collections.max(valueList) - Collections.min(valueList);
            Double average = valueList.stream().collect(Collectors.averagingDouble(x -> x));
            DecimalFormat df = new DecimalFormat("#.00");

            Instant instantStop = (Instant) fluxRecordList.get(0).getValueByKey("_stop");
            dateList.add(dateFormat.format(new Date(instantStop.toEpochMilli())));

            sensorDataList.put("date", dateList);
            sensorDataList.put("fieldName", fieldName);
            sensorDataList.put("value", valueList);
            sensorDataList.put("min", df.format(average - distance));
            sensorDataList.put("max", df.format(average + distance));
        }

        return sensorDataList;
    }
}
