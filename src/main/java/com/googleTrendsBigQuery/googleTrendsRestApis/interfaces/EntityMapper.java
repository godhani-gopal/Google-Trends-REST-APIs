package com.googleTrendsBigQuery.googleTrendsRestApis.interfaces;

import com.google.cloud.bigquery.FieldValueList;

@FunctionalInterface
public interface EntityMapper<T> {
    T map(FieldValueList values);
}
