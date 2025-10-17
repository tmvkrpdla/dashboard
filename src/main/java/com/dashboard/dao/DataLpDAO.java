package com.dashboard.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataLpDAO {

    Long getTotalUsageFifteenMinute(@Param("dtDttmHI") long dtDttmHI);
}
