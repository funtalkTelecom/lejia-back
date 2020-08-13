package com.lejia.mapper;

import com.github.abel533.mapper.Mapper;
import com.lejia.pojo.UserPermission;
import org.springframework.stereotype.Component;

@Component
public interface UserPermissionMapper extends Mapper<UserPermission>,BaseMapper<UserPermission> {
}