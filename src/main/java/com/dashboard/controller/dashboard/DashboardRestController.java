package com.dashboard.controller.dashboard;

import com.dashboard.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardRestController {


    private final SqlSessionTemplate sessionTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/totalMtr_ajax")
    public @ResponseBody Object getTotalMtrInfo(@RequestBody String selectedValuesParam) throws JsonProcessingException {

        Map<String, Object> selectedValues = parseSelectedValues(selectedValuesParam);
        log.info("selectedValues : {}", selectedValues);

        int todayDate = StringUtil.getTodayAsInt();
        String targetDate = null;

        // Map에서 targetDate 값을 가져와 처리
        if (selectedValues.containsKey("targetDate")) {
            Object targetDateObject = selectedValues.get("targetDate");

            if (targetDateObject instanceof List) {
                List<?> targetDateList = (List<?>) targetDateObject;

                // targetDateList가 비어 있지 않다면 첫 번째 값을 사용 (필요에 따라 조정 가능)
                if (!targetDateList.isEmpty()) {
                    targetDate = targetDateList.get(0).toString();
//                    log.info("targetDate: {}", targetDate);
                } else {
//                    log.info("targetDate 리스트가 비어 있습니다.");
                }
            } else {
//                log.info("targetDate가 List 형식이 아닙니다.");
            }
        }

        // targetDate가 null 또는 빈 문자열이면 todayDate로 설정
        if (targetDate == null || targetDate.isEmpty()) {
            targetDate = String.valueOf(todayDate);
            selectedValues.put("targetDate", targetDate);
        } else {
            selectedValues.put("targetDate", targetDate);
        }

        List<Map<String, Object>> totalData = sessionTemplate.selectList("siteMapper.getTotalMtrInfo", selectedValues);

        for (Map<String, Object> data : totalData) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String valueString = (value != null) ? value.toString() : "-";
                data.put(key, valueString);
            }
        }


        String jsonList = objectMapper.writeValueAsString(totalData);

        Map mp = new HashMap<>();
        mp.put("data", totalData);
        mp.put("jsonList", jsonList);

        return mp;
    }

    // JSON 형태의 선택된 값들을 맵으로 파싱하는 메소드
    public static Map<String, Object> parseSelectedValues(String selectedValuesParam) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON 문자열을 Map으로 변환
            return objectMapper.readValue(selectedValuesParam, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
            return null;
        }
    }
}
