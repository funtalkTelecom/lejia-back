package com.lejia.mapper;

import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.Page;
import com.lejia.pojo.Permission;
import com.lejia.pojo.Role;
import com.lejia.pojo.RolePermission;
import com.lejia.pojo.UserPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PermissionMapper extends Mapper<Permission>,BaseMapper<Permission>{

	public Page<Object> listRole(String name);

	public int rolePermissionCount(@Param("ids") String ids, @Param("roleId") int roleId);

	public int userPermissionCount(@Param("ids") String ids, @Param("userId") int userId);

	public List<Role> findRoleName(String name);

	public void addRole(@Param("role") Role role);

	public void deleByRoleId(@Param("roleId") int roleId);

	public void deleByUserId(@Param("userId") int userId);

	public void addRolePermission(@Param("param") RolePermission rp);

	public void addUserPermission(@Param("param") UserPermission up);

}
