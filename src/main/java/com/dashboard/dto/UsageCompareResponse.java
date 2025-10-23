package com.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageCompareResponse {


    private String rangeType;        // today, week, month, year
    private Long currentUsage;       // 현재 사용량

    private Long prevWeekUsage;      // 전주 또는 전년 동기 사용량
    private Double prevWeekPercent;  // ↑ 대비 증감률 (%)

    private Long prevMonthUsage;     // 전월 사용량
    private Double prevMonthPercent; // ↑ 대비 증감률 (%)
}