package com.dashboard.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayUtils {

    public static <T> List<List<T>> split(List<T> resList, int count) {
        if (resList == null || count < 1)
            return Collections.emptyList();
        List<List<T>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= count) {
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            if (last > 0) {
                List<T> itemList = new ArrayList<>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }
}
