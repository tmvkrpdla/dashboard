package com.dashboard.controller.dashboard;

import com.dashboard.dto.UsageCompareResponse;
import com.dashboard.service.DataLpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardRestController {

    private final DataLpService dataLpService;

    @GetMapping("/api/getTotalUsageFifteenMinute")
    public ResponseEntity<Long> getTotalUsageFifteenMinute(
            @RequestParam long dtDttmHI) {

        Long totalUsage = dataLpService.getTotalUsageFifteenMinute(dtDttmHI);
        return ResponseEntity.ok(totalUsage != null ? totalUsage : 0L);
    }


//    @GetMapping("/api/getTotalUsageForHour")
//    public ResponseEntity<Long> getTotalUsageForHour(
//            @RequestParam long startDateTarget, @RequestParam long endDateTarget) {
//
//        Long totalUsage = dataLpService.getTotalUsageForHour(startDateTarget, endDateTarget);
//        return ResponseEntity.ok(totalUsage != null ? totalUsage : 0L);
//    }


    @GetMapping("/api/getTotalUsageForHour")
    public ResponseEntity<Long> getTotalUsageForHour() throws Exception {
        Long totalUsage = dataLpService.getTotalUsageLastHourParallel();
        return ResponseEntity.ok(totalUsage);
    }

    @GetMapping("/api/getHourlyUsage")
    public List<Map<String, Object>> getHourlyUsage(@RequestParam("today") String today) {
        return dataLpService.getHourlyUsage(today);
    }


    @GetMapping("/api/getUsageByRange")
    public ResponseEntity<UsageCompareResponse> getUsageByRange(@RequestParam String rangeType) {
        UsageCompareResponse response = dataLpService.getUsageCompare(rangeType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/getYearlyUsageByDay")
    public ResponseEntity<List<Map<String, Object>>> getYearlyUsageByDay() {
        List<Map<String, Object>> data = dataLpService.getYearlyUsageByDay();
        return ResponseEntity.ok(data);
    }
}
