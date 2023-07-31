package com.sensor.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.sensor.common.AuthorizedException;
import com.sensor.util.FluxUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NavigateService {

    public Map<String, Object> getNavigateMapProcess(List<String> projectIdAuthorizeList) throws AuthorizedException {
        return getNavigateMap("boxName", "cdName",projectIdAuthorizeList);
    }

    public Map<String, Object> getNavigateMapDistribution(List<String> projectIdAuthorizeList) throws AuthorizedException {

        return getNavigateMap("chuanganqileixing", "boxName",projectIdAuthorizeList);
    }

    public Map<String, Object> getNavigateMap(String searchField_1, String searchField_2, List<String> projectIdAuthorizeList) throws AuthorizedException {

        String fluxImport = "import \"influxdata/influxdb/schema\"\n";
        String start = "2022";
        String bucket = "sensorFilter";
        InfluxDBClient influxDBClient = FluxUtil.createInfluxClient(bucket);
        QueryApi queryApi = influxDBClient.getQueryApi();

        String measurementFlux = String.format("%s schema.measurements(bucket: \"%s\", start:%s)", fluxImport,bucket,start);

        List<FluxTable> measurementTables = queryApi.query(measurementFlux);

        List<String> projectNameList = measurementTables.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());

        //Authorized filter
        List<String> unauthorizedList = projectNameList.stream().filter(item -> !projectIdAuthorizeList.contains(item)).collect(Collectors.toList());
        projectNameList = projectNameList.stream().filter(item -> projectIdAuthorizeList.contains(item)).collect(Collectors.toList());

        if(projectNameList.size() == 0){
            throw new AuthorizedException("No Authorized");
        }

        Map<String, Map<String, List<String>>> navigateMap = new HashMap<>();
        for(String projectName : projectNameList){
            String searchFieldFlux_1 = String.format("%s schema.tagValues(bucket: \"%s\", tag: \"%s\", start:%s, predicate:(r) => r[\"_measurement\"] == \"%s\")", fluxImport,bucket, searchField_1,start, projectName);

            List<FluxTable> searchFieldTables_1 = queryApi.query(searchFieldFlux_1);
            List<String> searchFieldList_1 = searchFieldTables_1.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());

            HashMap<String, List<String>> searchMap = new HashMap<>();
            for(String search : searchFieldList_1){
                String searchFieldFlux_2 = String.format("%s schema.tagValues(bucket: \"%s\", tag: \"%s\", start:%s, predicate:(r) => r[\"_measurement\"] == \"%s\" and r[\"%s\"] == \"%s\")", fluxImport, bucket, searchField_2, start, projectName, searchField_1, search);
                List<FluxTable> searchFieldTables_2 = queryApi.query(searchFieldFlux_2);
                List<String> searchFieldList_2 = searchFieldTables_2.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());
                searchMap.put(search, searchFieldList_2);
            }

            navigateMap.put(projectName,searchMap);
        }

        Map<String, Object> navigateAuthorizedMap = new HashMap<>();
        navigateAuthorizedMap.put("navigate", navigateMap);
        navigateAuthorizedMap.put("unauthorized", unauthorizedList);

        return navigateAuthorizedMap;
    }
    public Map<String, Object> getNavigateMapInclinometer(String sensorType, List<String> projectIdAuthorizeList) throws AuthorizedException {

        String fluxImport = "import \"influxdata/influxdb/schema\"\n";
        String start = "2022";
        String bucket = "sensorFilter";
        InfluxDBClient influxDBClient = FluxUtil.createInfluxClient(bucket);
        QueryApi queryApi = influxDBClient.getQueryApi();

        String measurementFlux = String.format("%s schema.tagValues(bucket: \"%s\", tag: \"_measurement\", start:%s, predicate:(r) => r[\"chuanganqileixing\"] == \"%s\")", fluxImport,bucket,start,sensorType);

        List<FluxTable> measurementTables = queryApi.query(measurementFlux);

        List<String> projectNameList = measurementTables.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());

        //Authorized filter
        List<String> unauthorizedList = projectNameList.stream().filter(item -> !projectIdAuthorizeList.contains(item)).collect(Collectors.toList());
        projectNameList = projectNameList.stream().filter(item -> projectIdAuthorizeList.contains(item)).collect(Collectors.toList());

        if(projectNameList.size() == 0){
            throw new AuthorizedException("No Authorized");
        }

        Map<String, List<String>> navigateMap = new HashMap<>();
        for(String projectName : projectNameList){
            String boxNameFlux = String.format("%s schema.tagValues(bucket: \"%s\", tag: \"boxName\", start:%s, predicate:(r) => r[\"_measurement\"] == \"%s\" and r[\"chuanganqileixing\"] == \"%s\")", fluxImport, bucket, start, projectName, sensorType);
            List<FluxTable> boxNameTables = queryApi.query(boxNameFlux);
            List<String> boxNameList = boxNameTables.get(0).getRecords().stream().map(fluxRecord -> fluxRecord.getValue().toString()).collect(Collectors.toList());
            navigateMap.put(projectName, boxNameList);
        }

        Map<String, Object> navigateAuthorizedMap = new HashMap<>();
        navigateAuthorizedMap.put("navigate", navigateMap);
        navigateAuthorizedMap.put("unauthorized", unauthorizedList);

        return navigateAuthorizedMap;
    }
}
