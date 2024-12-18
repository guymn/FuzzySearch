package com.fuzzy.search.utils;

import java.util.ArrayList;
import java.util.List;

public class NGramsUtils {

    public static List<String> generateNGrams(String originalString, int n) {
        List<String> nGrams = new ArrayList<>();
        if (n == 0) {
            return nGrams;
        }

        for (int i = 0; i < originalString.length() - (n - 1); i++) {
            nGrams.add(originalString.substring(i, i + n));
        }

        return nGrams;
    }

    public static List<String> getListConditionString(List<String> nGrams, String column) {
        List<String> listConditionString = new ArrayList<>();
        for (int i = 0; i < nGrams.size(); i++) {
            StringBuilder transformedStringBuilder = new StringBuilder();
            transformedStringBuilder.append("(LOWER(" + column + ") LIKE LOWER('%")
                    .append(nGrams.get(i))
                    .append("%')) ");
            listConditionString.add(transformedStringBuilder.toString());
        }

        return listConditionString;
    }

    public static String getConditionString(List<String> listCondition, String s) {
        StringBuilder conditionString = new StringBuilder();
        for (int i = 0; i < listCondition.size(); i++) {
            conditionString.append(listCondition.get(i));
            if (i < listCondition.size() - 1) {
                conditionString.append(s);
            }
        }

        return conditionString.toString();
    }

}
