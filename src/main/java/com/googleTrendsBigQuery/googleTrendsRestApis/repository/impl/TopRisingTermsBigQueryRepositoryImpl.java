package com.googleTrendsBigQuery.googleTrendsRestApis.repository.impl;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.BigQueryExecutor;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopRisingTermsRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Repository
public class TopRisingTermsBigQueryRepositoryImpl {
    QueryBuilder queryBuilder;
    TopRisingTermsRepository topRisingTermsRepository;
    BigQueryExecutor bigQueryExecutor;

    public TopRisingTermsBigQueryRepositoryImpl(QueryBuilder queryBuilder, TopRisingTermsRepository topRisingTermsRepository, BigQueryExecutor bigQueryExecutor) {
        this.queryBuilder = queryBuilder;
        this.topRisingTermsRepository = topRisingTermsRepository;
        this.bigQueryExecutor = bigQueryExecutor;
    }

    public List<TopRisingTerms> retrieveAndSaveMostRecentData() throws InterruptedException {
        String query = queryBuilder.loadDataFromTopRisingTermsQuery();
        TableResult result = bigQueryExecutor.executeQuery(query);

        List<TopRisingTerms> results = new ArrayList<>();
        Iterator<FieldValueList> iterator = result.iterateAll().iterator();
        int batchSize = 500;
        List<TopRisingTerms> batch = new ArrayList<>(batchSize);

        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Future<Void>> futures = new ArrayList<>();

            while (iterator.hasNext()) {
                FieldValueList values = iterator.next();
                TopRisingTerms topRisingTerms = mapToTopRisingTerms(values);
                batch.add(topRisingTerms);

                if (batch.size() >= batchSize) {
                    List<TopRisingTerms> currentBatch = new ArrayList<>(batch);
                    Future<Void> future = executor.submit(() -> {
                        saveBatch(currentBatch);
                        return null;
                    });
                    futures.add(future);
                    results.addAll(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                List<TopRisingTerms> currentBatch = new ArrayList<>(batch);
                Future<Void> future = executor.submit(() -> {
                    saveBatch(currentBatch);
                    return null;
                });
                futures.add(future);
                results.addAll(batch);
            }

            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new InterruptedException("Error occurred while saving batches: " + e.getMessage());
                }
            }
        }
        return results;
    }

    private void saveBatch(List<TopRisingTerms> batch) {
        topRisingTermsRepository.saveAll(batch);
    }

    private TopRisingTerms mapToTopRisingTerms(FieldValueList values) {
        TopRisingTerms topRisingTerms = new TopRisingTerms();
        topRisingTerms.setRank(getIntegerValueOrNull(values, "rank"));
        topRisingTerms.setPercentGain(getIntegerValueOrNull(values, "percent_gain"));
        topRisingTerms.setRefreshDate(getLocalDateValueOrNull(values, "refresh_date"));
        topRisingTerms.setDmaName(getStringValueOrNull(values, "dma_name"));
        topRisingTerms.setDmaId(getStringValueOrNull(values, "dma_id"));
        topRisingTerms.setTerm(getStringValueOrNull(values, "term"));
        topRisingTerms.setWeek(getLocalDateValueOrNull(values, "week"));
        topRisingTerms.setScore(getIntegerValueOrNull(values, "score"));
        return topRisingTerms;
    }

    private String getStringValueOrNull(FieldValueList values, String fieldName) {
        if (values.get(fieldName) == null || values.get(fieldName).isNull()) {
            return null;
        }
        return values.get(fieldName).getStringValue();
    }

    private LocalDate getLocalDateValueOrNull(FieldValueList values, String fieldName) {
        if (values.get(fieldName) == null || values.get(fieldName).isNull()) {
            return null;
        }
        try {
            return LocalDate.parse(values.get(fieldName).getStringValue());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private Integer getIntegerValueOrNull(FieldValueList values, String fieldName) {
        if (values.get(fieldName) == null || values.get(fieldName).isNull()) {
            return null;
        }
        return (int) values.get(fieldName).getLongValue();
    }


}