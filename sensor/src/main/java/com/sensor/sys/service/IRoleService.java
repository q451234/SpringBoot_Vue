package com.sensor.sys.service;

import com.sensor.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laocai
 * @since 2023-02-07
 */
public interface IRoleService extends IService<Role> {
    void addRole(Role role);

    Role getRoleById(Integer id);

    @Transactional
    void updateRole(Role role);

    void deleteRoleById(Integer id);
}
