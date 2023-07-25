package com.sensor.util;

import com.sensor.common.Pauta;
import com.sensor.entity.SensorData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PautaUtil {

    public static void removeException(List<SensorData> sensorDataList){
        Map<String, List<SensorData>> sensorDataMap = sensorDataList.stream().collect(
                Collectors.groupingBy(SensorData::getCdId)
        );

        for(String key: sensorDataMap.keySet()){
            List<SensorData> mapList = sensorDataMap.get(key);
            Pauta pauta = new Pauta(mapList);
            List<SensorData> exceptionList = pauta.judge();
            sensorDataList.removeAll(exceptionList);
            log.debug("异常数据来自传感器" + key + " 过滤数据大小: " + exceptionList.size());
        }
    }
}
