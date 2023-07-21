package com.sensor.sys.mapper;

import com.sensor.sys.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    @Select({
        "SELECT a.* FROM " +
        "menu a, role_menu b, user_role c " +
        "WHERE a.menu_id = b.menu_id " +
        "AND b.role_id = c.role_id " +
        "AND a.parent_id = #{pid} " +
        "AND c.user_id = #{userId}"
    })
    public List<Menu> getMenuListByUserId(@Param("userId") Integer userId,
                                          @Param("pid") Integer pid);
}
