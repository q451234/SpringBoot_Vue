package com.sensor.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sensor.entity.RoleData;
import com.sensor.entity.RoleMenu;
import com.sensor.mapper.RoleDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class RoleDataManager {
    @Autowired
    private RoleDataMapper roleDataMapper;

    public List<RoleData> getRoleDataList(){
        return roleDataMapper.getRoleDataList();
    }

    public List<RoleData> getRoleDataListByRoleId(Integer roleId){
        return roleDataMapper.getRoleDataListByRoleId(roleId);
    }

    public List<RoleData> getAllProject(){
        return roleDataMapper.getAllProject();
    }

    @Transactional
    public void deleteByRoleId(Integer id){
        LambdaQueryWrapper<RoleData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleData::getRoleId,id);
        roleDataMapper.delete(wrapper);
    }

    @Transactional
    public void insertRoleData(RoleData roleData){
        roleDataMapper.insert(roleData);
    }

    public String getProjectIdByProjectName(String projectName){
        return roleDataMapper.getProjectIdByProjectName(projectName);
    }
}
