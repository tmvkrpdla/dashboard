package com.dashboard.controller.dashboard;

import com.dashboard.service.DataLpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Long> getUsageByRange(@RequestParam String rangeType) {
        Long usage = dataLpService.getUsageByRange(rangeType);
        return ResponseEntity.ok(usage);
    }


}
