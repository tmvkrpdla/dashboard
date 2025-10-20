package com.dashboard.service;

import com.dashboard.dao.clickhouse.DataLpClickHouseDAO;
import com.dashboard.dao.mariadb.DataLpDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataLpService {
    private final DataLpDAO dataLpDAO;
    private final DataLpClickHouseDAO dataLpClickHouseDAO;

    public Long getTotalUsageFifteenMinute(long dtDttmHI) {
        return dataLpDAO.getTotalUsageFifteenMinute(dtDttmHI);
    }

    public Long getTotalUsageForHour(long startDateTarget, long endDateTarget) {
        return dataLpClickHouseDAO.getTotalUsageFifteenMinute(startDateTarget, endDateTarget);
    }

    public long getTotalUsageLastHourParallel() throws InterruptedException, ExecutionException {
        LocalDateTime now = LocalDateTime.now().minusMinutes(LocalDateTime.now().getMinute() % 15);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        List<Long> dtList = new ArrayList<>();
        for (int i = 4; i > 0; i--) {
            dtList.add(Long.parseLong(now.minusMinutes(15L * i).format(fmt)));
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Callable<Long>> tasks = dtList.stream()
                .map(dt -> (Callable<Long>) () -> dataLpDAO.getTotalUsageFifteenMinute(dt)
                )
                .collect(Collectors.toList());
        List<Future<Long>> results = executor.invokeAll(tasks);


        long totalUsage = 0L;
        for (Future<Long> f : results) {
            totalUsage += f.get();
        }

        log.info("Queried time slots: {}", dtList);
        log.info("Total usage for last hour: {}", totalUsage);


        executor.shutdown();
        return totalUsage;
    }


    public List<Map<String, Object>> getHourlyUsage(String today) {
        return dataLpClickHouseDAO.getHourlyUsage(today);
    }

   /* public Long getUsageByRange(String rangeType) {
        return dataLpClickHouseDAO.getUsageByRange(rangeType);
    }*/


    public Long getUsageByRange(String rangeType) {
        LocalDate today = LocalDate.now();
        String startDate = null;
        String endDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        switch (rangeType) {
            case "today":
                startDate = endDate;
                break;

            case "week":
                // 이번 주 월요일 (ISO 기준: Monday=1)
                LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);
                startDate = monday.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                break;

            case "month":
                LocalDate firstDayOfMonth = today.withDayOfMonth(1);
                startDate = firstDayOfMonth.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                break;

            case "year":
                LocalDate firstDayOfYear = today.withDayOfYear(1);
                startDate = firstDayOfYear.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                break;
        }


        log.info("rangeType : {}, startDate : {},  endDate: {}", rangeType, startDate, endDate);

        return dataLpClickHouseDAO.getUsageByRange(startDate, endDate);
    }


}
