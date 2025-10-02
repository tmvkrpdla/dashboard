package com.dashboard.util;//package system.co.kr.util;
//
//import com.google.gson.Gson;
//
//import java.net.URLEncoder;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Maps {
//    public static void main(String []ar){
//        Map map= Maps.of("1","2","3","4");
//        System.out.println(new Gson().toJson(map));
//    }
//
//    public static int getInt(Map<String,String> obj, String param){
//        try{
//            return Integer.parseInt(obj.get(param));
//        }catch(Exception e){
//            return 0;
//        }
//    }
//    public static Map<Object, Object> of(Object... entries) {
//        if (entries.length % 2 != 0) {
//            throw new IllegalArgumentException("Invalid number of arguments. Arguments must be in pairs.");
//        }
//
//        Map<Object, Object> map = new HashMap<>();
//        for (int i = 0; i < entries.length; i += 2) {
//            Object key = entries[i];
//            Object value = entries[i + 1];
//
//            if (key == null || value == null) {
//                throw new NullPointerException("Keys or values cannot be null.");
//            }
//
//            map.put(key, value);
//        }
//        return Collections.unmodifiableMap(map);
//    }
//
//    public static String mapToQueryString(Map<String, String> data) throws Exception {
//        StringBuilder query = new StringBuilder();
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            if (query.length() > 0) {
//                query.append('&');
//            }
//            query.append(entry.getKey()).append('=');
//            query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//        }
//        return query.toString();
//    }
//}
