package com.sensor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sensor.entity.Menu;
import com.sensor.entity.SensorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FetchMapper extends BaseMapper<Menu> {

    @Select({
            "select a.*, b.box_id, b.box_name, " +
                    "c.cd_id, c.cd_name, c.cgqlx_id, c.cgqlx_name, c.hole_id, " +
                    "d.l_max, d.l_min, d.unit, d.points, d.data_name, d.calculatedata_name, d.substand_name, " +
                    "e.clsj, e.ad, e.m_data, e.calculatedata, e.substand, e.temperature, e.m_mode, e.chuanganqileixing from t_project a " +
                    "left join t_box b " +
                    "on a.project_id = b.project_id " +
                    "left join t_cd c " +
                    "on b.project_id = c.project_id and b.box_id = c.box_id " +
                    "left join t_cgqlx d " +
                    "on a.project_id = d.project_id and c.cgqlx_id = d.cgqlx_id " +
                    "left join t_record e " +
                    "on c.cd_id = e.cd_id limit 10"
    })
    public List<SensorData> fetchSensorList();
}
