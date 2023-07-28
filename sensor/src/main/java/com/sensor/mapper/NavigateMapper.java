package com.sensor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sensor.entity.Menu;
import com.sensor.entity.Navigate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NavigateMapper extends BaseMapper<Menu> {

    @Select({
            "select * from t_relate "
    })
    public List<Navigate> getNavigateList();
}
