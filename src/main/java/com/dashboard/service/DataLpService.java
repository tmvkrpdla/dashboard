package com.dashboard.service;

import com.dashboard.dao.clickhouse.DataLpClickHouseDAO;
import com.dashboard.dao.mariadb.DataLpDAO;
import com.dashboard.dto.UsageCompareResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
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

//        log.info("Queried time slots: {}", dtList);
//        log.info("Total usage for last hour: {}", totalUsage);


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


//        log.info("rangeType : {}, startDate : {},  endDate: {}", rangeType, startDate, endDate);

        return dataLpClickHouseDAO.getUsageByRange(startDate, endDate);
    }


    public List<Map<String, Object>> getYearlyUsageByDay() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfYear = today.withDayOfYear(1);
        String thisYearStart = firstDayOfYear.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 예: 20250101

        return dataLpClickHouseDAO.getYearlyUsageByDay(thisYearStart);
    }


    public UsageCompareResponse getUsageCompare(String rangeType) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate curStart = null;
        LocalDate curEnd = today;

        // ✅ 현재 구간 설정
        switch (rangeType) {
            case "today":
                curStart = today;
                break;
            case "week":
                curStart = today.with(DayOfWeek.MONDAY);
                break;
            case "month":
                curStart = today.withDayOfMonth(1);
                break;
            case "year":
                curStart = today.withDayOfYear(1);
                break;
        }

        // ✅ 현재 사용량
        Long curUsage = dataLpClickHouseDAO.getUsageByRange(curStart.format(df), curEnd.format(df));

        Long prevWeekUsage = null;
        Long prevMonthUsage = null;

        // ✅ today, week → 기존 방식 유지 (전주 + 전월 비교)
        if (rangeType.equals("today") || rangeType.equals("week")) {
            long periodDays = ChronoUnit.DAYS.between(curStart, curEnd);

            // 전주 같은 기간
            LocalDate prevWeekStart = curStart.minusWeeks(1);
            LocalDate prevWeekEnd = prevWeekStart.plusDays(periodDays);
            prevWeekUsage = dataLpClickHouseDAO.getUsageByRange(prevWeekStart.format(df), prevWeekEnd.format(df));

            // 전월 같은 기간
            LocalDate prevMonthStart = curStart.minusMonths(1);
            LocalDate prevMonthEnd = prevMonthStart.plusDays(periodDays);
            prevMonthUsage = dataLpClickHouseDAO.getUsageByRange(prevMonthStart.format(df), prevMonthEnd.format(df));
        }

        // ✅ month → 전월 대비 + 전년 동기 대비
        if (rangeType.equals("month")) {
            // 전월: 지난달 1일 ~ 지난달 오늘 날짜
            LocalDate lastMonthStart = curStart.minusMonths(1);
            LocalDate lastMonthEnd = lastMonthStart.withDayOfMonth(today.getDayOfMonth());
            prevMonthUsage = dataLpClickHouseDAO.getUsageByRange(lastMonthStart.format(df), lastMonthEnd.format(df));

            // 전년 동기: 작년 이번달 1일 ~ 작년 오늘 날짜
            LocalDate lastYearMonthStart = curStart.minusYears(1);
            LocalDate lastYearMonthEnd = lastYearMonthStart.withDayOfMonth(today.getDayOfMonth());
            prevWeekUsage = dataLpClickHouseDAO.getUsageByRange(lastYearMonthStart.format(df), lastYearMonthEnd.format(df));
        }

        // ✅ year → 전년 동기 대비만
        if (rangeType.equals("year")) {
            LocalDate lastYearStart = curStart.minusYears(1);
            LocalDate lastYearEnd = curEnd.minusYears(1);
            prevWeekUsage = dataLpClickHouseDAO.getUsageByRange(lastYearStart.format(df), lastYearEnd.format(df));
        }

        // ✅ 증감률 계산 함수
        Function<Long, Double> calcPercent = (prev) -> {
            if (prev == null || prev == 0) return null;
            return ((double) (curUsage - prev) / prev) * 100;
        };

        Double prevWeekPercent = calcPercent.apply(prevWeekUsage);
        Double prevMonthPercent = calcPercent.apply(prevMonthUsage);

//        log.info("rangeType : {}, curStart : {},  curEnd: {}, prevWeekPercent : {} , prevMonthPercent : {} ", rangeType, curStart, curEnd, prevWeekPercent, prevMonthPercent);

        // ✅ 최종 반환
        return new UsageCompareResponse(
                rangeType,
                curUsage,
                prevWeekUsage,
                prevWeekPercent,
                prevMonthUsage,
                prevMonthPercent
        );
    }


}
