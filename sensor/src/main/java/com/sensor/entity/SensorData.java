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

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("project_name")
    @Column(measurement = true)
    private String projectName;

    @JsonProperty("box_id")
    @Column(tag = true)
    private String boxId;

    @JsonProperty("box_name")
    @Column(tag = true)
    private String boxName;

    @JsonProperty("cd_id")
    @Column(tag = true)
    private String cdId;

    @JsonProperty("cd_name")
    @Column(tag = true)
    private String cdName;

    @JsonProperty("cgqlx_id")
    @Column(tag = true)
    private String cgqlxId;

    @JsonProperty("cgqlx_name")
    private String cgqlxName;

    @JsonProperty("hole_id")
    @Column(tag = true)
    private String holeId;

    @JsonProperty("l_max")
    @Column(tag = true)
    private String lMax;

    @JsonProperty("l_min")
    @Column(tag = true)
    private String lMin;

    private String unit;

    @Column(tag = true)
    private String points;

    @JsonProperty("data_name")
    @Column(tag = true)
    private String dataName;

    @JsonProperty("calculatedata_name")
    @Column(tag = true)
    private String calculatedataName;

    @JsonProperty("substand_name")
    @Column(tag = true)
    private String substandName;

    private String clsj;

    @Column(timestamp = true)
    private Instant time;

    @Column
    private Double ad;

    @JsonProperty("m_data")
    @Column
    private Double mData;

    @Column
    private Double calculatedata;

    @Column
    private Double substand;

    @Column
    private Double temperature;

    @JsonProperty("m_mode")
    @Column(tag = true)
    private String mMode;

    @Column(tag = true)
    private String chuanganqileixing;
}
