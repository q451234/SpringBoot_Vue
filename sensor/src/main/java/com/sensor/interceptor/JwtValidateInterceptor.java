package com.sensor.interceptor;

import com.alibaba.fastjson2.JSON;
import com.sensor.common.Constant;
import com.sensor.common.Result;
import com.sensor.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        if(token != null){
            try {
                jwtUtil.parseToken(token);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info(request.getRequestURI() + " 禁止访问...");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.fail(Constant.FAIL_CODE_3,"登录信息无效，请重新登录")));
        return false;
    }
}

