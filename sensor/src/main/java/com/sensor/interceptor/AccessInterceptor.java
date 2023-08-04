package com.sensor.interceptor;


import com.sensor.common.Access;
import com.sensor.common.AccessLevel;
import com.sensor.entity.User;
import com.sensor.util.JwtUtil;
import com.sensor.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserUtil userUtil;
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Access access = method.getAnnotation(Access.class);
        if (access == null) {
            // 如果注解为null, 说明不需要拦截, 直接放过
            return true;
        }

        // 如果是所有都能访问权限直接放行
        if (access.level() == AccessLevel.NORMAL) {
            return true;
        }
        if (access.level().getCode() >= AccessLevel.NORMAL.getCode()) {
            //这里为自己写的获取登录用户的信息的方法，大家可以根据自己的方法修改
            User user = userUtil.getLoginUser(request);
            if (getAuth(user) < access.level().getCode()) {
                response.setStatus(403);
                log.info("access " + method.getName() + " No authority");
                return false;
            }
        }
        return true;
    }

    public Integer getAuth(User user){
        return userUtil.getAuth(user);
    }
}

