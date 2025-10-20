package com.dashboard.dao.clickhouse;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataLpClickHouseDAO {

    Long getTotalUsageFifteenMinute(@Param("startDateTarget") long startDateTarget,
                                    @Param("endDateTarget") long endDateTarget);


    List<Map<String, Object>> getHourlyUsage(@Param("today") String today);

    Long getUsageByRange(@Param("startDate") String startDate,
                         @Param("endDate") String endDate);

}
