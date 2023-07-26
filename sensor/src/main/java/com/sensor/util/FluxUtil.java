package com.sensor.util;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.sensor.common.Constant;

import java.util.List;

public class FluxUtil {

    public static InfluxDBClient createInfluxClient(String bucket){
        char[] token = Constant.INFLUXDB_TOKEN.toCharArray();
        String org = Constant.INFLUXDB_ORG;

        return InfluxDBClientFactory.create(Constant.INFLUXDB_URL, token, org, bucket);
    }
}
