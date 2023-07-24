package com.sensor.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.influxdb.annotations.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorData {

    @JsonProperty("项目ID")
    private String projectId;

    @JsonProperty("项目名称")
    @Column(measurement = true)
    private String projectName;

    @JsonProperty("采集仪ID")
    @Column(tag = true)
    private String boxId;

    @JsonProperty("采集仪名称")
    @Column(tag = true)
    private String boxName;

    @JsonProperty("测点ID")
    @Column(tag = true)
    private String cdId;

    @JsonProperty("测点名称")
    @Column(tag = true)
    private String cdName;

    @JsonProperty("传感器类型ID")
    @Column(tag = true)
    private String cgqlxId;

    @JsonProperty("传感器名称")
    private String cgqlxName;

    @JsonProperty("位置信息")
    @Column(tag = true)
    private String holeId;

    @JsonProperty("正常数据上限")
    @Column(tag = true)
    private String lMax;

    @JsonProperty("正常数据下限")
    @Column(tag = true)
    private String lMin;

    @JsonProperty("单位")
    private String unit;

    @JsonProperty("保留小数")
    @Column(tag = true)
    private String points;

    @JsonProperty("一次计算量名称")
    @Column(tag = true)
    private String dataName;

    @JsonProperty("二次计算量名称")
    @Column(tag = true)
    private String calculatedataName;

    @JsonProperty("三次计算量名称")
    @Column(tag = true)
    private String substandName;

    @JsonProperty("测量时间")
    private String clsj;

    @Column(timestamp = true)
    private Instant time;

    @JsonProperty("AD值")
    @Column
    private Double ad;

    @JsonProperty("测量值")
    @Column
    private Double mData;

    @JsonProperty("计算值")
    @Column
    private Double calculatedata;

    @JsonProperty("变化量")
    @Column
    private Double substand;

    @JsonProperty("温度")
    @Column
    private Double temperature;

    @JsonProperty("采集类型")
    @Column(tag = true)
    private String mMode;

    @JsonProperty("传感器类型")
    @Column(tag = true)
    private String chuanganqileixing;
}
