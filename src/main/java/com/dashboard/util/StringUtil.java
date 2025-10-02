package com.dashboard.util;

import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {
    public String getFullYearYYYY() {
        return String.valueOf(getFullYearInt());
    }

    public int getFullYearInt() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public String getMonthMM() {
        int month = getMonthInt();
        String temp = "";

        if (month < 10) {
            temp = "0" + month;
        } else {
            temp = String.valueOf(month);
        }

        return temp;
    }

    public String getMonthString() {

        return String.valueOf(getMonthInt());
    }

    public int getMonthInt() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public int getDayInt() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public String getDayDD() {
        int date = getDayInt();
        String temp = "";
        if (date < 10) {
            temp = "0" + date;
        } else {
            temp = String.valueOf(date);
        }
        return temp;
    }

    public String getDateYYMMDD() {
        return getFullYearYYYY() + "-" + getMonthMM() + "-" + getDayDD();
    }

    public String getToday() {
        return getFullYearYYYY() + getMonthMM() + getDayDD();
    }

    public String getTimeHHmmss() {
        SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");

        return format1.format(System.currentTimeMillis());
    }

    public String round3(String num) {
        BigDecimal bigDec = new BigDecimal(num);
        DecimalFormat formatter = new DecimalFormat("0.00");
        return formatter.format(bigDec);

    }

    public String toJsonString(List<Map<String, String>> itemList) {

        return new Gson().toJson(itemList);
    }


    public <T> List<List<T>> split(List<T> resList, int count) {
        if (resList == null || count < 1)
            return null;
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) {
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }

    public void formatAndPutDate(Map<String, Object> dcu, String key) {

        // 두 가지 가능한 형식 정의
        SimpleDateFormat inputFormatWithMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat inputFormatWithoutMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (dcu.containsKey(key)) {
            String originalDateStr = String.valueOf(dcu.get(key));
            Date date = null;

            // 두 가지 형식으로 파싱 시도
            try {
                date = inputFormatWithMillis.parse(originalDateStr);
            } catch (ParseException e) {
                try {
                    date = inputFormatWithoutMillis.parse(originalDateStr);
                } catch (ParseException e2) {
                    e2.printStackTrace();
                    return; // 형식이 맞지 않는 경우 함수 종료
                }
            }

            // 변환에 성공한 경우, 형식을 맞춰 저장
            if (date != null) {
                String formattedDateStr = outputFormat.format(date);
                dcu.put(key, formattedDateStr);
            }
        }
    }

    //    스트링객체꺼내기
    public String extractStringValue(JSONObject jsonObject, String key) {

        return (String) jsonObject.get(key);
    }


//    public static void formatDateFieldS(List<Map<String, Object>> dataList, String key) {
//        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        for (Map<String, Object> map : dataList) {
//            if (map.containsKey(key)) {
//                String originalDateStr = String.valueOf(map.get(key));
//                try {
//                    LocalDateTime dateTime = LocalDateTime.parse(originalDateStr, inputFormat);
//                    String formattedDateStr = dateTime.format(outputFormat);
//                    map.put(key, formattedDateStr);
//                } catch (DateTimeParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


    public static void formatDateFieldS(Object data, String key) {


//        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            if (dataMap.containsKey(key)) {
                String originalDateStr = String.valueOf(dataMap.get(key));
                if (originalDateStr != null && !originalDateStr.isEmpty() && !originalDateStr.equals("-")) {

                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(originalDateStr, inputFormat);
                        String formattedDateStr = dateTime.format(outputFormat);
                        dataMap.put(key, formattedDateStr);
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (data instanceof List) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
            for (Map<String, Object> map : dataList) {
                if (map.containsKey(key)) {
                    String originalDateStr = String.valueOf(map.get(key));

                    if (originalDateStr != null && !originalDateStr.isEmpty() && !originalDateStr.equals("-")) {

                        try {
                            LocalDateTime dateTime = LocalDateTime.parse(originalDateStr, inputFormat);
                            String formattedDateStr = dateTime.format(outputFormat);
                            map.put(key, formattedDateStr);
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } else {
            throw new IllegalArgumentException("Input data must be either Map<String, Object> or List<Map<String, Object>>");
        }
    }


    // 전화번호 정규식 Map, list<Map>, list<HashMap> 다 받기 가능
    public static void formatSenderPhones(Object inputObject, String fieldName) {
        if (inputObject instanceof Map) {
            formatSenderPhonesInMap((Map<String, Object>) inputObject, fieldName);
        } else if (inputObject instanceof List) {
            formatSenderPhonesInList((List<? extends Map<String, Object>>) inputObject, fieldName);
        } else {
            // 처리할 수 없는 객체 형식입니다.
            throw new IllegalArgumentException("Input object must be either Map or List<Map>");
        }
    }

    private static void formatSenderPhonesInMap(Map<String, Object> reception, String fieldName) {
        // 정규식 패턴
        String phonePattern = "^(02.{0}|01.{1}|[0-9]{3})([0-9]+)([0-9]{4})";
        Pattern pattern = Pattern.compile(phonePattern);

        // "senderPhone" 필드 가져오기
        Object senderPhoneObj = reception.get(fieldName);

        if (senderPhoneObj instanceof String) {
            String senderPhone = ((String) senderPhoneObj).trim();
            // 정규식 패턴과 일치하는지 확인
            Matcher matcher = pattern.matcher(senderPhone);
            if (matcher.matches()) {
                // 정규식 패턴과 일치하면 포맷팅
                String formattedValue = matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);
                reception.put(fieldName, formattedValue);
            }
        }
    }

    private static void formatSenderPhonesInList(List<? extends Map<String, Object>> receptionList, String fieldName) {
        // 정규식 패턴
        String phonePattern = "^(02.{0}|01.{1}|[0-9]{3})([0-9]+)([0-9]{4})";
        Pattern pattern = Pattern.compile(phonePattern);

        for (Map<String, Object> reception : receptionList) {
            // "senderPhone" 필드 가져오기
            Object senderPhoneObj = reception.get(fieldName);

            if (senderPhoneObj instanceof String) {
                String senderPhone = ((String) senderPhoneObj).trim();
                // 정규식 패턴과 일치하는지 확인
                Matcher matcher = pattern.matcher(senderPhone);
                if (matcher.matches()) {
                    // 정규식 패턴과 일치하면 포맷팅
                    String formattedValue = matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);
                    reception.put(fieldName, formattedValue);
                }
            }
        }
    }


    public static List<Map<String, String>> getNullFapList(List<Map<String, Object>> list_fap) {
        List<Map<String, String>> propertyList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int todayDate = getTodayAsInt();

        for (Map<String, Object> list_fap_map : list_fap) {
            String fap = String.valueOf(list_fap_map.get("fap"));
            String mid = String.valueOf(list_fap_map.get("meter_id"));
            String dtLastMeteringLp = String.valueOf(list_fap_map.get("dtLastMeteringLp"));
            if ("null".equals(fap) && !"null".equals(mid)) {
                Map<String, String> propertyMap = new HashMap<>();
                propertyMap.put("meter_id", String.valueOf(list_fap_map.get("meter_id")));
                propertyMap.put("dong_name", String.valueOf(list_fap_map.get("dong_name")));
                propertyMap.put("ho_name", String.valueOf(list_fap_map.get("ho_name")));
                propertyMap.put("dtLastMeteringLp", dtLastMeteringLp);

                if ("null".equals(dtLastMeteringLp)) {
                    propertyMap.put("rowColor", "red-color");
                } else {
                    LocalDateTime lastMeteringDate = LocalDateTime.parse(dtLastMeteringLp, formatter);
                    int lastMeteringDateInt = Integer.parseInt(lastMeteringDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    if (lastMeteringDateInt < todayDate) {
                        propertyMap.put("rowColor", "gray-color");
                    } else {
                        propertyMap.put("rowColor", "blue-color");
                    }
                }

                propertyList.add(propertyMap);
            }
        }
        return propertyList;
    }

    public static List<Map<String, String>> getEmptyFapList(List<Map<String, Object>> list_fap) {
        List<Map<String, String>> propertyList = new ArrayList<>();
        for (Map<String, Object> list_fap_map : list_fap) {
            String mid = String.valueOf(list_fap_map.get("meter_id"));
            String dtLastMeteringLp = String.valueOf(list_fap_map.get("dtLastMeteringLp"));
            if ("null".equals(dtLastMeteringLp) && !"null".equals(mid)) {
                Map<String, String> propertyMap = new HashMap<>();
                propertyMap.put("dong_name", String.valueOf(list_fap_map.get("dong_name")));
                propertyMap.put("ho_name", String.valueOf(list_fap_map.get("ho_name")));
                propertyMap.put("dtLastMeteringLp", String.valueOf(list_fap_map.get("dtLastMeteringLp")));
                propertyList.add(propertyMap);
            }
        }
        return propertyList;
    }


    public static List<Map<String, String>> getNoInstallList(List<Map<String, Object>> list_fap) {
        List<Map<String, String>> propertyList = new ArrayList<>();
        for (Map<String, Object> list_fap_map : list_fap) {
            String mid = String.valueOf(list_fap_map.get("meter_id"));
            if ("null".equals(mid)) {
                Map<String, String> propertyMap = new HashMap<>();
                propertyMap.put("dong_name", String.valueOf(list_fap_map.get("dong_name")));
                propertyMap.put("ho_name", String.valueOf(list_fap_map.get("ho_name")));
                propertyMap.put("dtLastMeteringLp", String.valueOf(list_fap_map.get("dtLastMeteringLp")));
                propertyList.add(propertyMap);
            }
        }
        return propertyList;
    }


    public static List<Map<String, String>> getNullRegFapList(List<Map<String, Object>> list_fap) {
        List<Map<String, String>> propertyList = new ArrayList<>();
        for (Map<String, Object> list_fap_map : list_fap) {
            String read_day_fap = String.valueOf(list_fap_map.get("read_day_fap"));
            String mid = String.valueOf(list_fap_map.get("meter_id"));

            if ("null".equals(read_day_fap) && !"null".equals(mid)) {
                Map<String, String> propertyMap = new HashMap<>();
                propertyMap.put("dong_name", String.valueOf(list_fap_map.get("dong_name")));
                propertyMap.put("ho_name", String.valueOf(list_fap_map.get("ho_name")));
                propertyMap.put("dtLastMeteringLp", String.valueOf(list_fap_map.get("dtLastMeteringLp")));
                propertyList.add(propertyMap);
            }
        }
        return propertyList;
    }


    public static void formatDateInList(List<Map<String, Object>> dataList, String dateKey) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Map<String, Object> data : dataList) {
            Object dtDttmObj = data.get(dateKey);
            if (dtDttmObj == null) {
                continue;
            }

            try {
                String dtDttm;
                if (dtDttmObj instanceof Long) {
                    dtDttm = dtDttmObj.toString();
                } else if (dtDttmObj instanceof Integer) {
                    dtDttm = (String) dtDttmObj.toString();
                } else if (dtDttmObj instanceof String) {
                    dtDttm = (String) dtDttmObj;
                } else {
                    throw new IllegalArgumentException("Unsupported data type for dtDttm: " + dtDttmObj.getClass().getName());
                }

                Date date = originalFormat.parse(dtDttm);
                String formattedDate = targetFormat.format(date);
                data.put(dateKey, formattedDate);
            } catch (ParseException e) {
                e.printStackTrace(); // Handle the exception as needed
            }
        }
    }


    // 오늘 날짜를 int 형식으로 반환하는 함수
    public static int getTodayAsInt() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = now.format(formatter);
        return Integer.parseInt(formattedDate);
    }

    public static int getYesterdayAsInt() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = yesterday.format(formatter);
        return Integer.parseInt(formattedDate);
    }


    // 오늘 날짜를 yyyy-mm-dd
    public static String getTodayAsHyphen() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }


    // 어제 날짜를 yyyy-MM-dd
    public static String getYesterdayAsHyphen() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return yesterday.format(formatter);
    }


    // 리스트를 업데이트하는 메서드
    public static List<Map<String, Object>> updateListWithTodayDate(List<Map<String, Object>> list, LocalDate today, DateTimeFormatter dateTimeFormatter) {

//        System.out.println("today: " + today);

        return list.stream()
                .map(map -> {

                    String dtLastMeteringLp = (String) map.get("dtLastMeteringLp");

                    if (dtLastMeteringLp != null && !dtLastMeteringLp.isEmpty()) {

                        try {
                            LocalDate meteringDate = LocalDateTime.parse(dtLastMeteringLp, dateTimeFormatter).toLocalDate();
                            if (meteringDate.equals(today)) {
                                map.put("dtLastMeteringLp", "-");
                            }
                        } catch (DateTimeParseException e) {
                            System.err.println("Error parsing date: " + dtLastMeteringLp);
                            // 필요한 경우 추가 처리를 수행하십시오.
                        }
                    }

                    return map;
                })
                .collect(Collectors.toList());
    }


    public static void yMdHmToyMd(Object data, String key) {

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            if (dataMap.containsKey(key)) {
                String originalDateStr = String.valueOf(dataMap.get(key));
                if (originalDateStr != null && !originalDateStr.isEmpty() && !originalDateStr.equals("-") && !originalDateStr.equals("null")) {

                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(originalDateStr, inputFormat);
                        String formattedDateStr = dateTime.format(outputFormat);
                        dataMap.put(key, formattedDateStr);
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (data instanceof List) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
            for (Map<String, Object> map : dataList) {
                if (map.containsKey(key)) {
                    String originalDateStr = String.valueOf(map.get(key));

                    if (originalDateStr != null && !originalDateStr.isEmpty() && !originalDateStr.equals("-") && !originalDateStr.equals("null")) {

                        try {
                            LocalDateTime dateTime = LocalDateTime.parse(originalDateStr, inputFormat);
                            String formattedDateStr = dateTime.format(outputFormat);
                            map.put(key, formattedDateStr);
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } else {
            throw new IllegalArgumentException("Input data must be either Map<String, Object> or List<Map<String, Object>>");
        }
    }


    public static void yMdHmsToyMd(Object data, String key) {

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            if (dataMap.containsKey(key)) {
                String originalDateStr = String.valueOf(dataMap.get(key));
                if (originalDateStr != null && !originalDateStr.isEmpty() && !originalDateStr.equals("-") && !originalDateStr.equals("null")) {

                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(originalDateStr, inputFormat);
                        String formattedDateStr = dateTime.format(outputFormat);
                        dataMap.put(key, formattedDateStr);
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (data instanceof List) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
            for (Map<String, Object> map : dataList) {
                if (map.containsKey(key)) {
                    String originalDateStr = String.valueOf(map.get(key));

                    if (originalDateStr != null && !originalDateStr.isEmpty() && !originalDateStr.equals("-") && !originalDateStr.equals("null")) {

                        try {
                            LocalDateTime dateTime = LocalDateTime.parse(originalDateStr, inputFormat);
                            String formattedDateStr = dateTime.format(outputFormat);
                            map.put(key, formattedDateStr);
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } else {
            throw new IllegalArgumentException("Input data must be either Map<String, Object> or List<Map<String, Object>>");
        }
    }

    public static String ifNull(Object value) {
        try {
            if (value == null) {
                return "";
            } else {
                return value.toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public int getModemSum(List<Map<String, Object>> list_fap) {
        int modemSum = 0;
        for (Map<String, Object> list_fap_map : list_fap) {
            if ((boolean) list_fap_map.get("is_bound_to_modem")) {
                modemSum++;
            }
        }
        return modemSum;
    }

}
