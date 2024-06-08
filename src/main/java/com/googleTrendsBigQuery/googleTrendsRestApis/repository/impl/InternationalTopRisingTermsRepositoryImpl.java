package com.googleTrendsBigQuery.googleTrendsRestApis.repository.impl;

import com.google.cloud.bigquery.*;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.InternationalTopRisingTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.QueryBuilderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InternationalTopRisingTermsRepositoryImpl implements InternationalTopRisingTermsRepository {

    private final BigQuery bigQuery;
    private final QueryBuilderRepository queryBuilderRepository;

    @Autowired
    public InternationalTopRisingTermsRepositoryImpl(BigQuery bigQuery,
                                                     QueryBuilderRepository queryBuilderRepository) {
        this.bigQuery = bigQuery;
        this.queryBuilderRepository = queryBuilderRepository;
    }

    @Override
    public List<String> getTopRisingTermsByCountry(String countryName, int limit) throws InterruptedException {

        String query = queryBuilderRepository.getTopRisingTermsInternationalQuery(countryName, limit);

        if (!query.equals("")) {
            TableResult result = getQueryResultFromBigQuery(query);
            System.out.println("queryResponse" + result);
            List<String> topTerms = new ArrayList<>();

            for (FieldValueList row : result.iterateAll()) {
                topTerms.add(row.get("term").getStringValue());
            }
            return topTerms;
        }
        return null;

    }

    private TableResult getQueryResultFromBigQuery(String query) throws InterruptedException {
        System.out.println("runningQuery ->" + query);
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).setUseQueryCache(false).build();
        Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).build());
        queryJob = queryJob.waitFor();

        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists!");
        }

        if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        return queryJob.getQueryResults();
    }


}
