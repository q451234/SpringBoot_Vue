package com.sensor.common;

import com.sensor.entity.RoleData;
import com.sensor.entity.User;
import com.sensor.mapper.RoleDataMapper;
import com.sensor.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataAuthorize {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RoleDataMapper roleDataMapper;

    public List<String> getProjectIdAuthorize(HttpServletRequest request){
        String token = request.getHeader("X-Token");
        User loginUser = jwtUtil.parseToken(token, User.class);

        List<RoleData> roleDataList = roleDataMapper.getRoleDataList();

        return roleDataList.stream().map(RoleData::getProjectName).collect(Collectors.toList());
    }
}
