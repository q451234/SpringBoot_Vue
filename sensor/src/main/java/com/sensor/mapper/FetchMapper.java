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
            "select e.*, b.box_name, a.project_name, " +
                    "c.project_id, c.box_id, c.cgqlx_id, c.cgqlx_name, c.hole_id, " +
                    "d.l_max, d.l_min, d.unit, d.points, d.data_name, d.calculatedata_name, d.substand_name " +
                    "from t_record e " +
                    "left join t_cd c " +
                    "on e.cd_id = c.cd_id " +
                    "left join t_cgqlx d " +
                    "on c.cgqlx_id = d.cgqlx_id " +
                    "left join t_box b " +
                    "on c.box_id = b.box_id " +
                    "left join t_project a " +
                    "on c.project_id = a.project_id"
    })
    public List<SensorData> fetchSensorList();
}
