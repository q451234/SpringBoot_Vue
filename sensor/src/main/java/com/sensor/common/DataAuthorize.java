package com.sensor.common;

import com.sensor.entity.Role;
import com.sensor.entity.RoleData;
import com.sensor.entity.User;
import com.sensor.entity.UserRole;
import com.sensor.manager.RoleDataManager;
import com.sensor.manager.UserRoleManager;
import com.sensor.service.RoleDataService;
import com.sensor.util.JwtUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataAuthorize {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleDataService roleDataService;

    @Autowired
    private UserRoleManager userRoleManager;

    public List<String> getProjectIdAuthorize(HttpServletRequest request){
        String token = request.getHeader("X-Token");
        User loginUser = jwtUtil.parseToken(token, User.class);

        List<UserRole> roleList = userRoleManager.getUserRoleListByUserId(loginUser.getId());

        List<RoleData> roleDataList = new ArrayList<>();
        for(UserRole userRole: roleList){
            roleDataList.addAll(roleDataService.getRoleDataListByRoleId(userRole.getRoleId()));
        }

        return roleDataList.stream().map(RoleData::getProjectName).collect(Collectors.toList());
    }
}
