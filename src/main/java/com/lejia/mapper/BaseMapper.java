package com.lejia.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
	
	public List<T> queryList(@Param("param") T t);

	public Page<Object> queryPageList(@Param("param") T t);

	public Page<Object> queryPageList(@Param("param") Map<String, Object> map);

	@Select("select `nextval`('lejia') ")
	@Options(useCache =false,flushCache=true)
	public int getId();
}
