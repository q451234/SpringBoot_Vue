package com.sensor.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.sensor.util.FluxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NavigateService {

    public Map<String, Map<String, List<String>>> getNavigateMap() {

        String fluxImport = "import \"influxdata/influxdb/schema\"\n";
        String start = "2022";
        String bucket = "sensorFilter";
        InfluxDBClient influxDBClient = FluxUtil.createInfluxClient(bucket);
        QueryApi queryApi = influxDBClient.getQueryApi();

        String measurementFlux = String.format("%s schema.measurements(bucket: \"%s\", start:%s)", fluxImport,bucket,start);

        List<FluxTable> measurementTables = queryApi.query(measurementFlux);

        List<String> projectNameList = measurementTables.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());

        Map<String, Map<String, List<String>>> navigateMap = new HashMap<>();
        for(String projectName : projectNameList){
            String boxNameFlux = String.format("%s schema.tagValues(bucket: \"%s\", tag: \"boxName\", start:%s, predicate:(r) => r[\"_measurement\"] == \"%s\")", fluxImport,bucket, start, projectName);

            List<FluxTable> boxNameTables = queryApi.query(boxNameFlux);
            List<String> boxNameList = boxNameTables.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());

            HashMap<String, List<String>> cdNameMap = new HashMap<>();
            for(String boxName : boxNameList){
                String cdNameFlux = String.format("%s schema.tagValues(bucket: \"%s\", tag: \"cdName\", start:%s, predicate:(r) => r[\"_measurement\"] == \"%s\" and r[\"boxName\"] == \"%s\")", fluxImport, bucket, start, projectName, boxName);
                List<FluxTable> cdNameTables = queryApi.query(cdNameFlux);
                List<String> cdNameList = cdNameTables.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());
                cdNameMap.put(boxName, cdNameList);
            }

            navigateMap.put(projectName,cdNameMap);
        }

        return navigateMap;
    }

}
