package com.lejia.mapper;

import com.github.abel533.mapper.Mapper;
import com.lejia.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper  extends Mapper<User>,BaseMapper<User>{

    public List<Map>  getPower(Integer id);
    public List<String> findRoles(Integer id);
    List<Map> finRolesByUserId(Integer id);
    void deleteRoleByUserId(@Param("userId") Integer userId);
    void insertUr(@Param("roleId") Integer roleId, @Param("userId") Integer userId);
}