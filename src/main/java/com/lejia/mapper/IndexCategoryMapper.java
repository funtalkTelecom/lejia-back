package com.lejia.mapper;

import com.github.abel533.mapper.Mapper;
import com.lejia.pojo.IndexCategory;

import java.util.List;

public interface IndexCategoryMapper extends Mapper<IndexCategory>,BaseMapper<IndexCategory> {

    List<IndexCategory>  selectAll();

 }