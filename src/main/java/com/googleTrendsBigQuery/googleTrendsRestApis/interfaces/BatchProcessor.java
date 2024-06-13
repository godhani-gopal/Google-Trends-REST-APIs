package com.googleTrendsBigQuery.googleTrendsRestApis.interfaces;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@FunctionalInterface
public interface BatchProcessor<T> {
    void process(List<T> batch, AtomicLong totalNumberOfSavedRecords);
}
