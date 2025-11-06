package com.dashboard.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.dashboard.service.DataLpService;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsageRangeScheduler {

    private final DataLpService dataLpService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 매일 새벽 3시 정각에 실행
     * month, year 데이터 갱신
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void updateMonthlyAndYearlyUsage() {
        log.info("🔄 [Scheduler] 매일 03:00 월별/연간 사용량 계산 시작");

        try {
            dataLpService.getUsageCompare("month");
            dataLpService.getUsageCompare("year");
            log.info("[Scheduler] 월별 및 연간 사용량 계산 완료");

            // 클라이언트에 갱신 신호 전송
            messagingTemplate.convertAndSend("/topic/usageUpdate", "refresh");
            log.info("[Scheduler] 월별 및 연간 사용량 계산 완료 & 신호 전송");

        } catch (Exception e) {
            log.error("[Scheduler] 월별/연간 사용량 계산 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
