package com.googleTrendsBigQuery.googleTrendsRestApis.repository.impl;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.googleTrendsBigQuery.googleTrendsRestApis.config.BigQueryExecutor;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.QueryExecutionException;
import com.googleTrendsBigQuery.googleTrendsRestApis.interfaces.BatchProcessor;
import com.googleTrendsBigQuery.googleTrendsRestApis.interfaces.EntityMapper;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Repository
public class BQRepositoryImpl implements BQRepository {

    public final QueryBuilder queryBuilder;
    public final BigQueryExecutor bigQueryExecutor;

    public BQRepositoryImpl(QueryBuilder queryBuilder, BigQueryExecutor bigQueryExecutor) {
        this.queryBuilder = queryBuilder;
        this.bigQueryExecutor = bigQueryExecutor;
    }

    @Override
    public <T> Long saveDataFromBQtoMySQL(Supplier<String> querySupplier,
                                          EntityMapper<T> entityMapper,
                                          BatchProcessor<T> batchProcessor) {
        String query = querySupplier.get();

        TableResult bqTableResult = null;
        try {
            bigQueryExecutor.executeQuery(query);
        } catch (InterruptedException e) {
            throw new QueryExecutionException("Error while running query: " + query, e.getMessage());
        }

        Iterator<FieldValueList> iterator = bqTableResult.iterateAll().iterator();
        AtomicLong totalNumberOfRecordsSaved = new AtomicLong(0);

        int batchSize = 500;
        List<T> batch = new ArrayList<>(batchSize);

        try (ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {

            while (iterator.hasNext()) {
                FieldValueList bqValues = iterator.next();
                T entity = entityMapper.map(bqValues);
                batch.add(entity);

                if (batch.size() > batchSize) {
                    List<T> currentBatch = new ArrayList<>(batch);
                    executorService.submit(() -> {
                        batchProcessor.process(currentBatch, totalNumberOfRecordsSaved);
                        return null;
                    });
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                List<T> currentBatch = new ArrayList<>(batch);
                executorService.submit(() -> {
                    batchProcessor.process(currentBatch, totalNumberOfRecordsSaved);
                    return null;
                });
                currentBatch.clear();
            }

            executorService.shutdown();
            boolean terminated = executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            if (terminated) {
                System.out.println("Total " + totalNumberOfRecordsSaved + " records downloaded from BigQuery and saved in MySQL.");
            }

            return totalNumberOfRecordsSaved.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}