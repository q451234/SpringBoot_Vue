package com.sensor.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.entity.SensorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class FileController {
    @PostMapping("/upload")
    public Result<?> fileUpload(@RequestBody List<SensorData> sensorDataList) throws ParseException {
        if(sensorDataList.size() == 0){
            return Result.fail(Constant.FAIL_CODE_4,"请上传文件");
        }
        log.info(sensorDataList.toString());
        uploadData(sensorDataList);
        return Result.success("上传成功");
    }


    public void uploadData(List<SensorData> sensorDataList) throws ParseException {
        char[] token = "XIpevgESCHb5kcGhmefA0L9EM80dXYwnz3R_lbl7usxoYEyZ2OXf0tyUimaC8BR-w4nxBWaSqd2Y1aWldkpvIQ==".toCharArray();
        String org = "CRDC";
        String bucket = "test";

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);

        // Write data
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        for(SensorData sensorData: sensorDataList){
            if(sensorData.getMData() != null){
                Date date = getTime(sensorData.getClsj());
                sensorData.setTime(date.toInstant());
//                writeApi.writeMeasurement( WritePrecision.MS, sensorData);
            }
        }
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