package com.googleTrendsBigQuery.googleTrendsRestApis.config;

import com.google.cloud.bigquery.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BigQueryExecutor {

    private final BigQuery bigQuery;

    public BigQueryExecutor(BigQuery bigQuery) {
        this.bigQuery = bigQuery;
    }

    public TableResult executeQuery(String query) throws InterruptedException {
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        queryJob = queryJob.waitFor();

        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }

        return queryJob.getQueryResults();
    }

}
