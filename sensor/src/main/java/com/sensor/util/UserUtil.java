package com.sensor.util;

import com.sensor.entity.User;
import com.sensor.manager.UserRoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class UserUtil {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRoleManager userRoleManager;
    public User getLoginUser(HttpServletRequest request){
        String token = request.getHeader("X-Token");
        User loginUser = jwtUtil.parseToken(token, User.class);
        List<Integer> roleIdList = userRoleManager.getRoleIdByUserId(loginUser.getId());
        loginUser.setRoleIdList(roleIdList);

        return loginUser;
    }
}
