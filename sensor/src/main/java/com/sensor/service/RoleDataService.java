package com.sensor.service;

import com.sensor.entity.Menu;
import com.sensor.entity.RoleData;
import com.sensor.manager.RoleDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleDataService{
    @Autowired
    private RoleDataManager roleDataManager;

    public List<RoleData> getAllRoleData() {
        return roleDataManager.getRoleDataList();
    }

    public List<RoleData> getAllProject(){
        return roleDataManager.getAllProject();
    }

    public List<RoleData> getRoleDataListByRoleId(Integer roleId){
        return roleDataManager.getRoleDataListByRoleId(roleId);
    }
}
