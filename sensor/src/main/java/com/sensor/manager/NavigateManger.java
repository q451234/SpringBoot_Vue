package com.sensor.manager;

import com.sensor.entity.Navigate;
import com.sensor.mapper.NavigateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class NavigateManger {
    @Autowired
    NavigateMapper navigateMapper;

    public List<Navigate> getNavigateList(){
        return navigateMapper.getNavigateList();
    }

}
