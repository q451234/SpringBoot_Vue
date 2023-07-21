package com.sensor.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sensor.sys.entity.Role;
import com.sensor.sys.entity.RoleMenu;
import com.sensor.sys.mapper.RoleMapper;
import com.sensor.sys.mapper.RoleMenuMapper;
import com.sensor.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laocai
 * @since 2023-02-07
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Override
    @Transactional
    public void addRole(Role role) {
        this.baseMapper.insert(role);
        if(role.getMenuIdList() != null){
            for(Integer menuId : role.getMenuIdList()){
                log.info(String.valueOf(menuId));
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = this.baseMapper.selectById(id);
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        // 更新role表
        this.updateById(role);
        // 清除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,role.getRoleId());
        roleMenuMapper.delete(wrapper);
        //新增权限
        for (Integer menuId : role.getMenuIdList()) {
            roleMenuMapper.insert(new RoleMenu(null,role.getRoleId(),menuId));
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        this.removeById(id);
        // 清除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        roleMenuMapper.delete(wrapper);
    }

}
