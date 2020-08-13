package com.lejia.mapper;

import com.github.abel533.mapper.Mapper;
import com.lejia.pojo.IndexData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IndexDataMapper  extends Mapper<IndexData>,BaseMapper<IndexData>{

    List<IndexData> selectByCategoryId(@Param("indexCatId") Integer indexCatId);

}