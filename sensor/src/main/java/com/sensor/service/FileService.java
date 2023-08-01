package com.sensor.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.sensor.common.Constant;
import com.sensor.entity.SensorData;
import com.sensor.util.FluxUtil;
import com.sensor.util.PautaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {
    public void uploadData(List<SensorData> sensorDataList) throws ParseException {

        upload("uploadOrigin", sensorDataList);

        int originSize = sensorDataList.size();
        sensorDataList = sensorDataList.stream().filter(sensorData ->
                        sensorData.getSubstand() <= Double.parseDouble(sensorData.getLMax()) &&
                        sensorData.getSubstand() >= Double.parseDouble(sensorData.getLMin()) &&
                        sensorData.getMData() <= Double.parseDouble(sensorData.getLMax()) &&
                        sensorData.getMData() >= Double.parseDouble(sensorData.getLMin()) &&
                        sensorData.getCalculatedata() <= Double.parseDouble(sensorData.getLMax()) &&
                        sensorData.getCalculatedata() >= Double.parseDouble(sensorData.getLMin())

        ).collect(Collectors.toList());
        log.info("阈值清洗完成, 清洗数据大小: " + (originSize - sensorDataList.size()));

        PautaUtil.removeException(sensorDataList);

        upload("uploadFilter", sensorDataList);
    }

    public void upload(String bucket, List<SensorData> sensorDataList) throws ParseException {
        InfluxDBClient influxDBClient = FluxUtil.createInfluxClient(bucket);
        PautaUtil.removeException(sensorDataList);
        // Write data
        WriteApi writeApi = influxDBClient.makeWriteApi();
        for(SensorData sensorData: sensorDataList){
            Date date = getTime(sensorData.getClsj());
            sensorData.setTime(date.toInstant());
            writeApi.writeMeasurement( WritePrecision.MS, sensorData);
        }

        writeApi.flush();
        writeApi.close();
    }

    public Date getTime(String ditNumber) throws ParseException {
        //如果不是数字
        if(!isNumeric(ditNumber)){
            return null;
        }
        //如果是数字 小于0则 返回
        BigDecimal bd = new BigDecimal(ditNumber);
        int days = bd.intValue();//天数
        int mills = (int) Math.round(bd.subtract(new BigDecimal(days)).doubleValue() * 24 * 3600);

        //获取时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, Calendar.JANUARY, 1);
        calendar.add(Calendar.DATE, days - 2);
        int hour = mills / 3600;
        int minute = (mills - hour * 3600) / 60;
        int second = mills - hour * 3600 - minute * 60;
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        return calendar.getTime();
    }

    //校验是否数据含小数点
    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+\\.*[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
