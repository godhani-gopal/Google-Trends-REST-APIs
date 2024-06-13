package com.googleTrendsBigQuery.googleTrendsRestApis.util;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BQFieldValueUtil {

    public static String getStringValueOrNull(FieldValueList values, String fieldName) {
        FieldValue fieldValue = values.get(fieldName);
        return (fieldValue != null && !fieldValue.isNull()) ? fieldValue.getStringValue() : null;
    }

    public static LocalDate getLocalDateValueOrNull(FieldValueList values, String fieldName) {
        FieldValue fieldValue = values.get(fieldName);
        if (fieldValue != null && !fieldValue.isNull()) {
            try {
                return LocalDate.parse(fieldValue.getStringValue());
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    public static Integer getIntegerValueOrNull(FieldValueList values, String fieldName) {
        FieldValue fieldValue = values.get(fieldName);
        return (fieldValue != null && !fieldValue.isNull()) ? (int) fieldValue.getLongValue() : null;
    }
}
