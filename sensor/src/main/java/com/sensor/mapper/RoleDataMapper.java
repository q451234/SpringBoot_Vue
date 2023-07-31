package com.sensor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sensor.entity.RoleData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleDataMapper extends BaseMapper<RoleData> {
    @Select({
            "SELECT a.*, b.project_name FROM role_data a left join t_project b on a.project_id = b.project_id"
    })
    List<RoleData> getRoleDataList();

    @Select({
            "SELECT a.*, b.project_name FROM role_data a " +
            "left join t_project b on a.project_id = b.project_id where role_id = #{roleId}"
    })
    List<RoleData> getRoleDataListByRoleId(@Param("roleId") Integer roleId);

    @Select({"SELECT * FROM sensor.t_project"})
    List<RoleData> getAllProject();

    @Select({"SELECT t.project_id FROM t_project t where t.project_name = #{projectName}"})
    String getProjectIdByProjectName(String projectName);
}
