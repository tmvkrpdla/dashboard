package com.dashboard.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class DateUtils {
    public static String getDate(String strtype, Date date){
        java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
        return df.format(date).toString();
    }
    public static long getMinutesDifference(String isoDateString) {
        try {
            // 현재 시간을 UTC 시간대로 가져옴
            Instant now = Instant.now();

            // ISO 8601 형식의 문자열을 Instant 객체로 변환
            Instant date = Instant.parse(isoDateString);

            // 두 시간의 차이를 Duration 객체로 계산
            Duration duration = Duration.between(date, now);

            // Duration을 분 단위로 변환
            return duration.toMinutes();
        } catch(Exception e){
            return 0;
        }
    }
}
