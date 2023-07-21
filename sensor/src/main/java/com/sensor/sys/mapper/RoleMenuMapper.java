package com.sensor.sys.mapper;

import com.sensor.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author laocai
 * @since 2023-02-07
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Integer> getMenuIdListByRoleId(Integer roleId);
}
