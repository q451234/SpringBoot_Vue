package com.sensor.sys.mapper;

import com.sensor.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Select({
        "select a.menu_id " +
        "from role_menu a, menu b " +
        "where a.menu_id = b.menu_id " +
        "and b.is_leaf = 'Y' " +
        "and a.role_id = #{roleId}"
    })
    List<Integer> getMenuIdListByRoleId(Integer roleId);
}
