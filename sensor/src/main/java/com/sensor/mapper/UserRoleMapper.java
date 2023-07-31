package com.sensor.mapper;

import com.sensor.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select({
            "SELECT b.role_name " +
                    "FROM user_role a, role b " +
                    "WHERE a.role_id = b.role_id " +
                    "AND a.user_id = #{userId}"
    })
    public List<String> getRoleNameByUserId(Integer userId);

    @Select({
            "SELECT b.role_id " +
                    "FROM user_role a, role b " +
                    "WHERE a.role_id = b.role_id " +
                    "AND a.user_id = #{userId}"
    })
    public List<Integer> getRoleIdByUserId(Integer userId);

    @Select({
            "SELECT a.user_id FROM user_role a WHERE a.role_id = #{roleId}"
    })
    public List<Integer> getUserIdByRoleId(Integer roleId);
}
