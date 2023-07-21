package com.sensor.sys.mapper;

import com.sensor.sys.entity.UserRole;
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
}
