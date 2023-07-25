package com.sensor.common;

import com.sensor.entity.SensorData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;



public class Pauta {    //创建拉依达类
    private final List<SensorData> sensorDataList;         //接受原始数组

    private final double adAverage;
    private final double mDataAverage;
    private final double calculateDataAverage;
    private final double subStandAverage;

    private final double adStandardVariance;
    private final double mDataStandardVariance;
    private final double calculateDataStandardVariance;
    private final double subStandStandardVariance;

    public Pauta(List<SensorData> sensorDataList) {    //原始数组的算数平均值方法
        this.sensorDataList = sensorDataList;
        this.adAverage = sensorDataList.stream().mapToDouble(SensorData::getAd).average().getAsDouble();
        this.mDataAverage = sensorDataList.stream().mapToDouble(SensorData::getMData).average().getAsDouble();
        this.calculateDataAverage = sensorDataList.stream().mapToDouble(SensorData::getCalculatedata).average().getAsDouble();
        this.subStandAverage = sensorDataList.stream().mapToDouble(SensorData::getSubstand).average().getAsDouble();

        double adSum = 0;
        double mDataSum = 0;
        double calculateDataSum = 0;
        double subStandSum = 0;
        for(SensorData sensorData: sensorDataList){
            adSum += Math.pow(sensorData.getAd() - adAverage, 2);
            mDataSum += Math.pow(sensorData.getMData() - mDataAverage, 2);
            calculateDataSum += Math.pow(sensorData.getCalculatedata() - calculateDataAverage, 2);
            subStandSum += Math.pow(sensorData.getSubstand() - subStandAverage, 2);
        }

        this.adStandardVariance = Math.sqrt(adSum / (sensorDataList.size() - 1));
        this.mDataStandardVariance = Math.sqrt(mDataSum / (sensorDataList.size() - 1));
        this.calculateDataStandardVariance = Math.sqrt(calculateDataSum / (sensorDataList.size() - 1));
        this.subStandStandardVariance = Math.sqrt(subStandSum / (sensorDataList.size() - 1));
    }

    public List<SensorData> judge() {              //判断异常值方法，若异常，则输出
        List<SensorData> exceptionData = new ArrayList<>();

        for(SensorData sensorData: sensorDataList){
            if(Math.abs(sensorData.getAd() - adAverage) > (3 * adStandardVariance)){
                exceptionData.add(sensorData);
            }
            else if(Math.abs(sensorData.getMData() - mDataAverage) > (3 * mDataStandardVariance)){
                exceptionData.add(sensorData);
            }
            else if(Math.abs(sensorData.getCalculatedata() - calculateDataAverage) > (3 * calculateDataStandardVariance)){
                exceptionData.add(sensorData);
            }
            else if(Math.abs(sensorData.getSubstand() - subStandAverage) > (3 * subStandStandardVariance)){
                exceptionData.add(sensorData);
            }
        }

        return exceptionData;
    }
}