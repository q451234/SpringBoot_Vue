package com.sensor.service;

import com.sensor.entity.Menu;
import com.sensor.entity.Navigate;
import com.sensor.manager.MenuManger;
import com.sensor.manager.NavigateManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NavigateService {

    @Autowired
    NavigateManger navigateManger;

    public Map<String, Object> getNavigateMap() {
        Map<String, Object> navigateMap = new HashMap<>();

        List<Navigate> navigateList = navigateManger.getNavigateList();

        navigateList = navigateList.stream().filter(navigate -> navigate.getBoxName() != null && navigate.getCdName() != null && navigate.getProjectName() != null).collect(Collectors.toList());

        List<String> projectNameList = navigateList.stream().map(Navigate::getProjectName).distinct().collect(Collectors.toList());

        Map<String, Set<String>> projectNameMap = navigateList.stream().collect(Collectors.groupingBy(Navigate::getProjectName, Collectors.mapping(Navigate::getBoxName, Collectors.toSet())));


        Map<String, Map<String, Set<String>>> cdNameMap = new HashMap<>();

        for(String key: projectNameMap.keySet()){
            List<Navigate> navigateListProjectName = navigateList.stream().filter(navigate -> navigate.getProjectName().equals(key)).collect(Collectors.toList());
            Map<String, Set<String>> boxNameMap = navigateListProjectName.stream().collect(Collectors.groupingBy(Navigate::getBoxName, Collectors.mapping(Navigate::getCdName, Collectors.toSet())));
            cdNameMap.put(key, boxNameMap);
        }

        navigateMap.put("projectNameList", projectNameList);
        navigateMap.put("projectNameMap", projectNameMap);
        navigateMap.put("cdNameMap", cdNameMap);

        return navigateMap;
    }

}
