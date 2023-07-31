package com.sensor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sensor.entity.RoleData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleDataMapper extends BaseMapper<RoleData> {
    @Select({
            "SELECT a.*, b.project_name FROM role_data a left join t_project b on a.project_id = b.project_id"
    })
    List<RoleData> getRoleDataList();
}
