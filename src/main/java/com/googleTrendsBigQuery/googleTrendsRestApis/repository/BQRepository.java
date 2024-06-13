package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.interfaces.BatchProcessor;
import com.googleTrendsBigQuery.googleTrendsRestApis.interfaces.EntityMapper;
import org.springframework.stereotype.Repository;

import java.util.function.Supplier;

@Repository
public interface BQRepository {

    <T> Long saveDataFromBQtoMySQL(Supplier<String> querySupplier, EntityMapper<T> entityMapper, BatchProcessor<T> batchProcessor);
}
